package pl.tkadziolka.snipmeandroid.ui.preview

import androidx.navigation.NavController
import pl.tkadziolka.snipmeandroid.util.extension.navigateSafe

class PreviewNavigator {

    fun goToDetail(navController: NavController, snippetId: String) {
        navController.navigateSafe(PreviewFragmentDirections.goToDetail(snippetId))
    }
}