package pl.tkadziolka.snipmeandroid.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.tkadziolka.snipmeandroid.bridge.MainModel
import pl.tkadziolka.snipmeandroid.bridge.SessionModel
import pl.tkadziolka.snipmeandroid.ui.detail.DetailViewModel
import pl.tkadziolka.snipmeandroid.ui.donate.DonateViewModel
import pl.tkadziolka.snipmeandroid.ui.edit.EditViewModel
import pl.tkadziolka.snipmeandroid.ui.login.LoginViewModel
import pl.tkadziolka.snipmeandroid.ui.main.MainViewModel
import pl.tkadziolka.snipmeandroid.ui.preview.PreviewViewModel
import pl.tkadziolka.snipmeandroid.ui.session.SessionViewModel
import pl.tkadziolka.snipmeandroid.ui.share.ShareViewModel
import pl.tkadziolka.snipmeandroid.ui.splash.SplashViewModel

internal val viewModelModule = module {
    viewModel { SplashViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get(), get(), get()) }
    viewModel { SessionViewModel(get()) }
    viewModel { MainViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { PreviewViewModel(get(), get()) }
    viewModel { DetailViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { EditViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { ShareViewModel(get(), get(), get()) }
    viewModel { DonateViewModel(get()) }
}

internal val modelModule = module {
//    viewModel { SplashViewModel(get(), get()) }
//    viewModel { LoginViewModel(get(), get(), get(), get()) }
    single { SessionModel(get()) }
    single { MainModel(get(), get(), get(), get(), get(), get(), get()) }
//    viewModel { PreviewViewModel(get(), get()) }
//    viewModel { DetailViewModel(get(), get(), get(), get(), get(), get(), get()) }
//    viewModel { EditViewModel(get(), get(), get(), get(), get(), get()) }
//    viewModel { ShareViewModel(get(), get(), get()) }
//    viewModel { DonateViewModel(get()) }
}