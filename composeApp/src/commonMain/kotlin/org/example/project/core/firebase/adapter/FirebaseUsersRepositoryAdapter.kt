package org.example.project.core.firebase.adapter

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.example.project.core.database.entities.ContactEntity
import org.example.project.core.database.entities.UserEntity
import org.example.project.core.firebase.repository.FirebaseUserRepository
import org.example.project.features.friends.domain.repository.UsersRepository

/**
 * Adapter to connect Firebase user repository with domain users repository interface
 */
class FirebaseUsersRepositoryAdapter(
    private val firebaseUserRepository: FirebaseUserRepository
) : UsersRepository {
    
    override suspend fun searchUsers(query: String): Result<List<UserEntity>> {
        return try {
            val result = firebaseUserRepository.searchUsers(query)
            result.map { firebaseUsers ->
                firebaseUsers.map { firebaseUser ->
                    UserEntity(
                        userId = firebaseUser.uid,
                        name = firebaseUser.name,
                        email = firebaseUser.email,
                        username = firebaseUser.username,
                        avatarUrl = firebaseUser.avatarUrl,
                        phoneNumber = firebaseUser.phoneNumber,
                        createdAtEpochMillis = firebaseUser.createdAt
                    )
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getUserById(userId: String): Result<UserEntity?> {
        return try {
            val result = firebaseUserRepository.getUserById(userId)
            result.map { firebaseUser ->
                firebaseUser?.let {
                    UserEntity(
                        userId = it.uid,
                        name = it.name,
                        email = it.email,
                        username = it.username,
                        avatarUrl = it.avatarUrl,
                        phoneNumber = it.phoneNumber,
                        createdAtEpochMillis = it.createdAt
                    )
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getCurrentUser(): Result<UserEntity?> {
        return try {
            val result = firebaseUserRepository.getCurrentUserProfile()
            result.map { firebaseUser ->
                firebaseUser?.let {
                    UserEntity(
                        userId = it.uid,
                        name = it.name,
                        email = it.email,
                        username = it.username,
                        avatarUrl = it.avatarUrl,
                        phoneNumber = it.phoneNumber,
                        createdAtEpochMillis = it.createdAt
                    )
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateCurrentUser(user: UserEntity): Result<Unit> {
        // TODO: Convert UserEntity to FirebaseUser and update
        return Result.success(Unit)
    }
    
    override suspend fun syncContacts(contacts: List<ContactEntity>): Result<Unit> {
        // TODO: Implement contact synchronization with Firebase
        return Result.success(Unit)
    }
    
    override fun getContacts(): Flow<List<ContactEntity>> {
        // TODO: Implement getting contacts from Firebase
        return flowOf(emptyList())
    }
    
    override fun searchContacts(query: String): Flow<List<ContactEntity>> {
        // TODO: Implement contact search
        return flowOf(emptyList())
    }
    
    override suspend fun checkUsersExist(phoneNumbers: List<String>, emails: List<String>): Result<List<UserEntity>> {
        return try {
            val phoneResult = firebaseUserRepository.searchUsersByPhoneNumbers(phoneNumbers)
            val emailResult = firebaseUserRepository.searchUsersByEmails(emails)
            
            val phoneUsers = phoneResult.getOrElse { emptyList() }
            val emailUsers = emailResult.getOrElse { emptyList() }
            
            val allUsers = (phoneUsers + emailUsers).distinctBy { it.uid }.map { firebaseUser ->
                UserEntity(
                    userId = firebaseUser.uid,
                    name = firebaseUser.name,
                    email = firebaseUser.email,
                    username = firebaseUser.username,
                    avatarUrl = firebaseUser.avatarUrl,
                    phoneNumber = firebaseUser.phoneNumber,
                    createdAtEpochMillis = firebaseUser.createdAt
                )
            }
            
            Result.success(allUsers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
