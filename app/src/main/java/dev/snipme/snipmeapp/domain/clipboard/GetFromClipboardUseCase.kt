package dev.snipme.snipmeapp.domain.clipboard

import android.content.ClipboardManager
import timber.log.Timber

private const val MAIN_CLIP = 0

class GetFromClipboardUseCase(private val manager: ClipboardManager) {
    operator fun invoke(): String? =
        try {
            manager.primaryClip?.getItemAt(MAIN_CLIP)?.text.toString()
        } catch (e: Exception) {
            Timber.e("Couldn't get text from clipboard, error = $e")
            null
        }
}