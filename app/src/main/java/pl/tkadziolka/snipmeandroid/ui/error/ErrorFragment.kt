package pl.tkadziolka.snipmeandroid.ui.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_error.*
import pl.tkadziolka.snipmeandroid.BuildConfig
import pl.tkadziolka.snipmeandroid.R
import pl.tkadziolka.snipmeandroid.util.extension.navigateUpTo
import pl.tkadziolka.snipmeandroid.util.extension.onBackPressed
import pl.tkadziolka.snipmeandroid.util.extension.safeOpenWebsite
import pl.tkadziolka.snipmeandroid.util.extension.setOnClick

class ErrorFragment: Fragment() {

    private val args: ErrorFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_error, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorMessage.text = args.message ?: getString(R.string.error_message_generic)

        onBackPressed { navigateToPreviousSuccessScreen() }
        errorClose.setOnClick { navigateToPreviousSuccessScreen() }
    }

    private fun navigateToPreviousSuccessScreen() {
        val nav = findNavController()
        val previousNavigation = nav.previousBackStackEntry
        val errorScreen = previousNavigation?.destination ?: return
        when(errorScreen.id) {
            // Error on main list blocks all app, so close it
            R.id.main -> requireActivity().finishAndRemoveTask()
            R.id.share -> nav.navigateUpTo(R.id.detail)
            R.id.login -> nav.navigateUp()
            else -> nav.navigateUpTo(R.id.main)
        }
    }
}