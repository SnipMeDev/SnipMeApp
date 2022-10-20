package pl.tkadziolka.snipmeandroid.ui.preview

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import pl.tkadziolka.snipmeandroid.domain.clipboard.AddToClipboardUseCase

class PreviewViewModel(
    private val navigator: PreviewNavigator,
    private val clipboard: AddToClipboardUseCase
): ViewModel() {

    fun goToDetail(nav: NavController, snippetId: String) {
        navigator.goToDetail(nav, snippetId)
    }

    fun copyToClipboard(label: String, code: String) {
        clipboard(label, code)
    }
}