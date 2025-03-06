package dev.snipme.snipmeapp.domain.snippet

import dev.snipme.snipmeapp.AppService
import dev.snipme.snipmeapp.domain.snippets.Snippet

class SaveSnippetUseCase(
    private val appService: AppService
) {
    operator fun invoke(image: ByteArray, snippet: Snippet): String {
        val name = "${snippet.title.replace(" ", "_")}.png"
        appService.storeFile(image, name, temp = false)
        appService.storeMediaFile(image, name)
        return name
    }
}