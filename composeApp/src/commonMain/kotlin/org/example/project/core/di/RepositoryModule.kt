package org.example.project.core.di

/**
 * DEPRECATED: This module has been replaced by Koin dependency injection.
 * See AppModule.kt for the new Koin-based dependency injection setup.
 * 
 * This file is kept temporarily for reference and will be removed once
 * all migrations are complete and tested.
 */
@Deprecated(
    message = "Use Koin dependency injection instead. See AppModule.kt",
    replaceWith = ReplaceWith("appModules", "org.example.project.core.di.appModules")
)
object RepositoryModule {
    // This object is deprecated and should not be used
}
