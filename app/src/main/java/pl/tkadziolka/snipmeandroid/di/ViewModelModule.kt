package pl.tkadziolka.snipmeandroid.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.tkadziolka.snipmeandroid.bridge.main.MainModel
import pl.tkadziolka.snipmeandroid.bridge.session.SessionModel
import pl.tkadziolka.snipmeandroid.ui.detail.DetailViewModel
import pl.tkadziolka.snipmeandroid.ui.edit.EditViewModel
import pl.tkadziolka.snipmeandroid.ui.login.LoginViewModel
import pl.tkadziolka.snipmeandroid.ui.main.MainViewModel
import pl.tkadziolka.snipmeandroid.ui.session.SessionViewModel
import pl.tkadziolka.snipmeandroid.ui.splash.SplashViewModel

internal val viewModelModule = module {
    viewModel { SplashViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get(), get(), get()) }
    viewModel { SessionViewModel(get()) }
    viewModel { MainViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { DetailViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { EditViewModel(get(), get(), get(), get(), get(), get()) }
}