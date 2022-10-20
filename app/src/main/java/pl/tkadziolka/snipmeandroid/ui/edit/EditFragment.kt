package pl.tkadziolka.snipmeandroid.ui.edit

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_edit.*
import pl.tkadziolka.snipmeandroid.R
import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet
import pl.tkadziolka.snipmeandroid.ui.Dialogs
import pl.tkadziolka.snipmeandroid.ui.viewmodel.ViewModelFragment
import pl.tkadziolka.snipmeandroid.ui.viewmodel.observeNotNull
import pl.tkadziolka.snipmeandroid.util.extension.*

class EditFragment : ViewModelFragment<EditViewModel>(EditViewModel::class) {
    // Update toolbar title when nav controller set up toolbar's destination
    private val destinationChange = NavController.OnDestinationChangedListener { _, _, _ ->
        editToolbar.title = if (isEdit()) getString(R.string.edit) else getString(R.string.create)
    }

    private val args by navArgs<EditFragmentArgs>()

    override val layout: Int = R.layout.fragment_edit

    private lateinit var dialogs: Dialogs
    private var loadingDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupExitDialogOnBack()
    }

    override fun onViewCreated() {
        dialogs = Dialogs(requireContext())
        setupToolbar()
        setupActions()
        viewModel.load(args.id)
    }

    override fun observeViewModel() {
        viewModel.state.observeNotNull(this) { state ->
            hideLoading()
            when (state) {
                Loading -> showLoading()
                is Loaded -> {
                    showData(state.languages, state.snippet)
                    showValidationError(state.error)
                }
                is Completed -> {
                    viewModel.goToDetail(findNavController(), state.snippet.uuid)
                }
                is Error -> {
                    viewModel.goToError(findNavController(), state.error)
                }
            }
        }

        viewModel.event.observeNotNull(this) { event ->
            when (event) {
                is Logout -> viewModel.goToLogin(findNavController())
                is Alert -> showToast(event.message)
                is PastedFromClipboard -> {
                    editCode.setText(event.code)
                    updatePasteButtonVisibility()
                }
            }
        }
    }

    private fun showData(languages: List<String>, snippet: Snippet?) {
        // Check if data is different
        val adapter = editLanguage.adapter
        if (adapter == null || adapter.count != languages.size) {
            editLanguage.setAdapter(getLanguageAdapter(languages))
        }

        snippet?.let { updatedSnippet ->
            getDifferingFields(updatedSnippet).forEach {
                if (it.id == R.id.editName) editName.setText(updatedSnippet.title)
                if (it.id == R.id.editCode) editCode.setText(updatedSnippet.code.raw)
                if (it.id == R.id.editLanguage) editLanguage.setText(updatedSnippet.language.raw)
            }
        }

        updatePasteButtonVisibility()
    }

    private fun showLoading() {
        loadingDialog = dialogs.loading.show()
    }

    private fun hideLoading() {
        loadingDialog?.dismiss()
    }

    private fun showValidationError(error: String?) {
        with(editError) {
            if (error == null) {
                gone()
                return
            }
            visible()
            text = error
        }
    }

    private fun applyChange() {
        getTextsAndUpdateState()
        if (isEdit()) {
            viewModel.edit(args.id ?: "")
        } else {
            viewModel.create()
        }
    }

    private fun setupActions() {
        editPasteFromClipboard.setOnClick {
            viewModel.pasteFromClipboard()
        }
    }

    private fun getTextsAndUpdateState() {
        val title = editName.getTyped()
        val code = editCode.text.toString()
        val language = editLanguage.getTyped()
        viewModel.update(title, code, language)
        updatePasteButtonVisibility()
    }

    private fun getDifferingFields(snip: Snippet): List<View> {
        val differingFields = mutableListOf<View>()

        if (editName.getTyped() != snip.title) {
            differingFields.add(editName)
        }

        if (editCode.text.toString() != snip.code.raw) {
            differingFields.add(editCode)
        }

        if (editLanguage.getTyped() != snip.language.raw) {
            differingFields.add(editLanguage)
        }

        return differingFields
    }


    private fun setupToolbar() {
        with(editToolbar) {
            // setupWithNavController sets action bar with default implementation
            setupWithNavController(findNavController())
            // So, each customization must be done after
            setNavigationOnClickListener { requireActivity().onBackPressed() }
            findNavController().addOnDestinationChangedListener(destinationChange)
            inflateMenu(R.menu.edit_menu)
            setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.editMenuSave -> { applyChange(); true }
                    else -> false
                }
            }
        }
    }

    private fun isEdit(): Boolean = args.id != null

    private fun getLanguageAdapter(languages: List<String>) = ArrayAdapter(
        requireContext(),
        android.R.layout.simple_dropdown_item_1line,
        languages
    )

    private fun setupExitDialogOnBack() {
        onBackPressed {
            val snippet = (viewModel.state.value as? Loaded)?.snippet

            when {
                snippet == null -> { back(); return@onBackPressed }
                isEdit().not() && fieldsAreEmpty() -> { back(); return@onBackPressed }
                getDifferingFields(snippet).isEmpty() -> { back(); return@onBackPressed }
            }

            dialogs.quit
                .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .setPositiveButton(R.string.okay) { dialog, _ ->
                    dialog.dismiss()
                    back()
                }.show()
        }
    }

    private fun updatePasteButtonVisibility() {
        if (editCode.text.toString().isNotBlank()) {
            editPasteFromClipboard.gone()
        }
    }

    private fun fieldsAreEmpty() =
        editName.getTyped().isEmpty()
                && editCode.getTyped().isEmpty()
                && editLanguage.getTyped().isEmpty()

    override fun onStop() {
        findNavController().removeOnDestinationChangedListener(destinationChange)
        super.onStop()
    }
}