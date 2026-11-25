package org.example.project.di

import com.russhwolf.settings.Settings
import org.example.project.data.auth.AuthApiService
import org.example.project.data.auth.KtorAuthApiService
import org.example.project.data.auth.RemoteAuthRepository
import org.example.project.data.network.createHttpClient
import org.example.project.data.repository.SettingsOnboardingRepository
import org.example.project.data.security.KSafeTokenStorage
import org.example.project.data.security.TokenStorage
import org.example.project.data.session.SessionEvents
import org.example.project.domain.repository.AuthRepository
import org.example.project.domain.repository.OnboardingRepository
import org.example.project.domain.usecase.CompleteOnboardingUseCase
import org.example.project.domain.usecase.GetCurrentUserUseCase
import org.example.project.domain.usecase.IsOnboardingCompletedUseCase
import org.example.project.domain.usecase.IsUserLoggedInUseCase
import org.example.project.domain.usecase.LoginUseCase
import org.example.project.domain.usecase.LogoutUseCase
import org.example.project.domain.usecase.ObserveSessionExpiredUseCase
import org.example.project.domain.usecase.ValidatePasswordUseCase
import org.example.project.domain.usecase.ValidateUsernameUseCase
import org.example.project.ui.auth.AuthViewModel
import org.example.project.ui.home.HomeViewModel
import org.example.project.ui.main.MainViewModel
import org.example.project.ui.onboarding.OnboardingViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * "Рецепт" складання застосунку: Koin сам створить обʼєкти й передасть їм залежності
 * у конструктор. KSafe приходить з [platformModule], бо на кожній платформі він створюється по-різному.
 */
val dataModule = module {
    single<Settings> { Settings() }
    singleOf(::SettingsOnboardingRepository) bind OnboardingRepository::class

    singleOf(::KSafeTokenStorage) bind TokenStorage::class
    singleOf(::SessionEvents)
    single { createHttpClient(tokenStorage = get(), sessionEvents = get()) }
    singleOf(::KtorAuthApiService) bind AuthApiService::class
    singleOf(::RemoteAuthRepository) bind AuthRepository::class
}

val domainModule = module {
    factoryOf(::IsOnboardingCompletedUseCase)
    factoryOf(::CompleteOnboardingUseCase)
    factoryOf(::ValidateUsernameUseCase)
    factoryOf(::ValidatePasswordUseCase)
    factoryOf(::LoginUseCase)
    factoryOf(::LogoutUseCase)
    factoryOf(::IsUserLoggedInUseCase)
    factoryOf(::GetCurrentUserUseCase)
    factoryOf(::ObserveSessionExpiredUseCase)
}

val presentationModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
}

val appModules = listOf(platformModule, dataModule, domainModule, presentationModule)
