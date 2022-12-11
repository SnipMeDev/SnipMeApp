package pl.tkadziolka.snipmeandroid.ui.login

import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_login.*
import pl.tkadziolka.snipmeandroid.BuildConfig
import pl.tkadziolka.snipmeandroid.R
import pl.tkadziolka.snipmeandroid.ui.Dialogs
import pl.tkadziolka.snipmeandroid.ui.viewmodel.ViewModelFragment
import pl.tkadziolka.snipmeandroid.ui.viewmodel.observeNotNull
import pl.tkadziolka.snipmeandroid.util.extension.*

class LoginFragment : ViewModelFragment<LoginViewModel>(LoginViewModel::class) {

    private val args: LoginFragmentArgs by navArgs()

    override val layout: Int = R.layout.fragment_login

    private lateinit var dialogs: Dialogs
    private var loadingDialog: AlertDialog? = null

    override fun onViewCreated() {
        dialogs = Dialogs(requireContext())

        if (BuildConfig.DEBUG)
            loginVersion.text = "v${BuildConfig.VERSION_NAME}"

        setupActions()
        viewModel.startup(args.login)
    }

    override fun observeViewModel() {
        viewModel.state.observeNotNull(this) { state ->
            hideLoading()
            when (state) {
                Loading -> showLoading()
                is Startup -> {
                    showOnlyLogin(state.login)
                    showValidationError(state.error)
                }
                is Login -> {
                    setRegisterViewState(isRegister = false)
                    showValidationError(state.error)
                }
                is Register -> {
                    setRegisterViewState(isRegister = true)
                    showValidationError(state.error)
                }
                is UserFound -> showUserFound()
                is Completed -> viewModel.goToMain(findNavController())
            }

            viewModel.event.observeNotNull(this) { event ->
                hideLoading()
                when (event) {
                    is Error -> viewModel.goToError(findNavController(), event.message)
                }
            }
        }
    }

    private fun setupActions() {
        loginAction.setOnClick {
            when (viewModel.state.value) {
                is Startup -> viewModel.validateLogin(loginName.text.trim())
                is Login -> viewModel.login(
                    loginName.text.trim(),
                    loginPassword.text.trim()
                )
                is Register -> viewModel.register(
                    loginName.text.trim(),
                    loginPassword.text.trim(),
                    loginPasswordRepeat.text.trim(),
                    loginEmail.text.trim()
                )
                else -> Unit
            }
        }

        loginRegisterAction.setOnClick {
            when (viewModel.state.value) {
                is Startup -> viewModel.register("", "", "", "")
                else -> Unit
            }
        }
    }

    private fun showLoading() {
        loadingDialog = dialogs.loading.show()
    }

    private fun hideLoading() {
        loadingDialog?.dismiss()
    }

    private fun showOnlyLogin(login: String?) {
        val phrase = getString(R.string.call_login)
        loginHelper.text = phrase
        login?.let { loginName.text = login }
        loginName.imeOptions = IME_ACTION_DONE
    }

    private fun setRegisterViewState(isRegister: Boolean) {
        loginImage.isVisible = isRegister.not()
        loginTitle.isVisible = isRegister.not()
        loginPassword.isVisible = true
        loginPasswordRepeat.isVisible = isRegister
        loginEmail.isVisible = isRegister
        loginRegisterAction.isVisible = false

        val imeOption = if (isRegister) {
            IME_ACTION_NEXT
        } else {
            IME_ACTION_DONE
        }

        val helperText = if (isRegister) {
            getString(R.string.call_register)
        } else {
            getString(R.string.call_login)
        }

        loginName.imeOptions = IME_ACTION_NEXT
        loginPassword.imeOptions = imeOption
        loginHelper.text = helperText
    }

    private fun showValidationError(error: String?) {
        with(loginError) {
            if (error == null) {
                gone()
                return
            }
            visible()
            text = error
        }
    }

    private fun showUserFound() {
        dialogs.alreadyRegistered
            .setPositiveButton(R.string.okay) { dialog, _ ->
                viewModel.goToLogin(viewModel.getLogin().orEmpty())
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                viewModel.revokeLogin(viewModel.getLogin().orEmpty())
                dialog.dismiss()
            }.show()
    }

    private fun showDialog(message: String) {
        dialogs.ok.setMessage(message).show()
    }
}