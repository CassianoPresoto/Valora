package org.example.project.core.firebase.usecase

import org.example.project.core.firebase.model.FirebaseUser
import org.example.project.core.firebase.repository.FirebaseUserRepository
import org.example.project.features.friends.domain.model.Contact

/**
 * Use case for searching users in Firebase
 */
class SearchUsersUseCase(
    private val userRepository: FirebaseUserRepository
) {
    /**
     * Search users by query (name, email, username)
     */
    suspend fun searchByQuery(query: String, limit: Int = 20): Result<List<FirebaseUser>> {
        return if (query.isBlank()) {
            Result.success(emptyList())
        } else {
            userRepository.searchUsers(query.trim(), limit)
        }
    }
    
    /**
     * Search users by phone numbers (for contact matching)
     */
    suspend fun searchByPhoneNumbers(phoneNumbers: List<String>): Result<List<FirebaseUser>> {
        return if (phoneNumbers.isEmpty()) {
            Result.success(emptyList())
        } else {
            // Clean phone numbers (remove formatting)
            val cleanNumbers = phoneNumbers.map { phone ->
                phone.replace(Regex("[^0-9]"), "")
            }.filter { it.isNotBlank() }
            
            userRepository.searchUsersByPhoneNumbers(cleanNumbers)
        }
    }
    
    /**
     * Search users by email addresses
     */
    suspend fun searchByEmails(emails: List<String>): Result<List<FirebaseUser>> {
        return if (emails.isEmpty()) {
            Result.success(emptyList())
        } else {
            val validEmails = emails.filter { email ->
                email.isNotBlank() && email.contains("@")
            }
            
            userRepository.searchUsersByEmails(validEmails)
        }
    }
    
    /**
     * Find users from device contacts
     */
    suspend fun findUsersFromContacts(contacts: List<Contact>): Result<List<Pair<Contact, FirebaseUser>>> {
        return try {
            val phoneNumbers = contacts.mapNotNull { it.phoneNumber }
            val emails = contacts.mapNotNull { it.email }
            
            // Search by phone numbers
            val usersByPhone = if (phoneNumbers.isNotEmpty()) {
                searchByPhoneNumbers(phoneNumbers).getOrElse { emptyList() }
            } else {
                emptyList()
            }
            
            // Search by emails
            val usersByEmail = if (emails.isNotEmpty()) {
                searchByEmails(emails).getOrElse { emptyList() }
            } else {
                emptyList()
            }
            
            // Combine results and match with contacts
            val allUsers = (usersByPhone + usersByEmail).distinctBy { it.uid }
            val matches = mutableListOf<Pair<Contact, FirebaseUser>>()
            
            contacts.forEach { contact ->
                val matchingUser = allUsers.find { user ->
                    // Match by phone
                    (contact.phoneNumber != null && 
                     user.phoneNumber?.replace(Regex("[^0-9]"), "") == 
                     contact.phoneNumber.replace(Regex("[^0-9]"), "")) ||
                    // Match by email
                    (contact.email != null && 
                     user.email.equals(contact.email, ignoreCase = true))
                }
                
                if (matchingUser != null) {
                    matches.add(Pair(contact, matchingUser))
                }
            }
            
            Result.success(matches)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
