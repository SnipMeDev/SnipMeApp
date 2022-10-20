package pl.tkadziolka.snipmeandroid.ui.splash

import androidx.navigation.fragment.findNavController
import pl.tkadziolka.snipmeandroid.ui.viewmodel.ViewModelFragment
import pl.tkadziolka.snipmeandroid.ui.viewmodel.observeNotNull

class SplashFragment : ViewModelFragment<SplashViewModel>(SplashViewModel::class) {

    override val layout: Int = pl.tkadziolka.snipmeandroid.R.layout.fragment_splash

    override fun onViewCreated() {
        viewModel.init()
    }

    override fun observeViewModel() {
        viewModel.state.observeNotNull(this) { state ->
            when(state) {
                is Logged -> viewModel.goToMain(findNavController())
                else -> viewModel.goToLogin(findNavController())
            }
        }
    }

}