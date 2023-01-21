package pl.tkadziolka.snipmeandroid.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.tkadziolka.snipmeandroid.ui.session.SessionViewModel

internal val viewModelModule = module {
    viewModel { SessionViewModel(get()) }
}