package dev.snipme.snipmeapp.domain.share

import dev.snipme.snipmeapp.AppService
import dev.snipme.snipmeapp.domain.snippets.Snippet

class ShareSnippetUseCase(
    private val appService: AppService
) {

    operator fun invoke(image: ByteArray, snippet: Snippet) {
        val name = "${appService.getCurrentDateFormatted()}.png"
        appService.storeFile(image, name, temp = true)
        appService.launchShareIntent(snippet)
    }
}