package pl.tkadziolka.snipmeandroid.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.tkadziolka.snipmeandroid.ui.login.LoginViewModel
import pl.tkadziolka.snipmeandroid.ui.main.MainViewModel
import pl.tkadziolka.snipmeandroid.ui.session.SessionViewModel

internal val viewModelModule = module {
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { SessionViewModel(get()) }
    viewModel { MainViewModel(get(), get(), get(), get(), get(), get(), get()) }
}