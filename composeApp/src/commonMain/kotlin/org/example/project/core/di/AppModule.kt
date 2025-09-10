package org.example.project.core.di

import org.koin.dsl.module
import org.example.project.core.firebase.adapter.FirebaseFriendsRepositoryAdapter
import org.example.project.core.firebase.adapter.FirebaseUsersRepositoryAdapter
import org.example.project.core.firebase.data.MockFirebaseAuthRepository
import org.example.project.core.firebase.data.MockFirebaseFriendsRepository
import org.example.project.core.firebase.data.MockFirebaseUserRepository
import org.example.project.core.firebase.repository.FirebaseAuthRepository
import org.example.project.core.firebase.repository.FirebaseFriendsRepository
import org.example.project.core.firebase.repository.FirebaseUserRepository
import org.example.project.core.firebase.usecase.CreateUserProfileUseCase
import org.example.project.core.firebase.usecase.ManageFriendshipUseCase
import org.example.project.core.firebase.usecase.SearchUsersUseCase
import org.example.project.features.friends.domain.repository.FriendsRepository
import org.example.project.features.friends.domain.repository.UsersRepository
import org.example.project.features.friends.domain.usecase.AddFriendUseCase
import org.example.project.features.friends.domain.usecase.GetFriendsUseCase
import org.example.project.features.friends.domain.usecase.GetTotalDebtSummaryUseCase
import org.example.project.features.friends.domain.usecase.SearchFriendsUseCase
import org.example.project.features.friends.domain.usecase.SearchUsersUseCase as DomainSearchUsersUseCase
import org.example.project.features.friends.presentation.AddFriendViewModel
import org.example.project.features.friends.presentation.FriendsViewModel
import org.example.project.features.profile.presentation.ProfileSetupViewModel

/**
 * Koin module for Firebase repositories
 */
val firebaseModule = module {
    // Firebase repositories (mock implementations for now)
    single<FirebaseAuthRepository> { MockFirebaseAuthRepository() }
    single<FirebaseUserRepository> { MockFirebaseUserRepository() }
    single<FirebaseFriendsRepository> { MockFirebaseFriendsRepository() }
}

/**
 * Koin module for repository adapters
 */
val adapterModule = module {
    // Firebase adapters to connect with domain layer
    single<FriendsRepository> { 
        FirebaseFriendsRepositoryAdapter(get<FirebaseFriendsRepository>(), get<FirebaseUserRepository>()) 
    }
    single<UsersRepository> { 
        FirebaseUsersRepositoryAdapter(get<FirebaseUserRepository>()) 
    }
}

/**
 * Koin module for use cases
 */
val useCaseModule = module {
    // Firebase Use Cases
    single { CreateUserProfileUseCase(get<FirebaseUserRepository>()) }
    single { SearchUsersUseCase(get<FirebaseUserRepository>()) }
    single { ManageFriendshipUseCase(get<FirebaseFriendsRepository>()) }
    
    // Domain Use Cases
    single { GetFriendsUseCase(get<FriendsRepository>()) }
    single { SearchFriendsUseCase(get<FriendsRepository>()) }
    single { AddFriendUseCase(get<FriendsRepository>()) }
    single { GetTotalDebtSummaryUseCase(get<FriendsRepository>()) }
    single { DomainSearchUsersUseCase(get<UsersRepository>()) }
}

/**
 * Koin module for ViewModels
 */
val viewModelModule = module {
    factory { FriendsViewModel(get(), get(), get()) }
    factory { AddFriendViewModel(get(), get()) }
    factory { ProfileSetupViewModel(get(), get<FirebaseUserRepository>()) }
}

/**
 * All app modules combined
 */
val appModules = listOf(
    firebaseModule,
    adapterModule,
    useCaseModule,
    viewModelModule
)
