package dev.snipme.snipmeapp.domain.clipboard

import android.content.ClipData
import android.content.ClipboardManager

class AddToClipboardUseCase(private val manager: ClipboardManager) {
    operator fun invoke(label: String, text: String) {
        val clip = ClipData.newPlainText(label, text)
        manager.setPrimaryClip(clip)
    }
}