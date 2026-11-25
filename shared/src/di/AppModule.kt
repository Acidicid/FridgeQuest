package org.example.project.di

import com.russhwolf.settings.Settings
import org.example.project.data.repository.FakeAuthRepository
import org.example.project.data.repository.SettingsOnboardingRepository
import org.example.project.domain.repository.AuthRepository
import org.example.project.domain.repository.OnboardingRepository
import org.example.project.domain.usecase.CompleteOnboardingUseCase
import org.example.project.domain.usecase.IsOnboardingCompletedUseCase
import org.example.project.domain.usecase.SignInUseCase
import org.example.project.domain.usecase.ValidatePasswordUseCase
import org.example.project.domain.usecase.ValidateUsernameUseCase
import org.example.project.ui.auth.AuthViewModel
import org.example.project.ui.main.MainViewModel
import org.example.project.ui.onboarding.OnboardingViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * "Рецепт" складання застосунку: Koin сам створить обʼєкти й передасть їм залежності
 * у конструктор, нам не треба писати `AuthViewModel(SignInUseCase(FakeAuthRepository(Settings())))`.
 */
val dataModule = module {
    single<Settings> { Settings() }
    singleOf(::SettingsOnboardingRepository) bind OnboardingRepository::class
    singleOf(::FakeAuthRepository) bind AuthRepository::class
}

val domainModule = module {
    factoryOf(::IsOnboardingCompletedUseCase)
    factoryOf(::CompleteOnboardingUseCase)
    factoryOf(::ValidateUsernameUseCase)
    factoryOf(::ValidatePasswordUseCase)
    factoryOf(::SignInUseCase)
}

val presentationModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::AuthViewModel)
}

val appModules = listOf(dataModule, domainModule, presentationModule)
