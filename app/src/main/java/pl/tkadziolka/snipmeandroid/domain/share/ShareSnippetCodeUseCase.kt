package pl.tkadziolka.snipmeandroid.domain.share

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet

class ShareSnippetCodeUseCase(
    private val context: Context
) {

    operator fun invoke(snippet: Snippet) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, snippet.code.raw)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, snippet.title)
        startActivity(context, shareIntent, null)
    }
}