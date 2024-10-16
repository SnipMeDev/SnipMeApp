package dev.snipme.snipmeapp.domain.share

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.core.content.ContextCompat.startActivity
import dev.snipme.snipmeapp.domain.snippets.Snippet

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
        shareIntent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, shareIntent, null)
    }
}