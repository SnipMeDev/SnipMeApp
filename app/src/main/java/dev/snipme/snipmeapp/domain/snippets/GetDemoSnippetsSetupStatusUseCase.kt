package dev.snipme.snipmeapp.domain.snippets

import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository

class GetDemoSnippetsSetupStatusUseCase(
    private val snippetRepository: SnippetRepository
) {

    operator fun invoke(): Boolean {
        return snippetRepository.getDemoSetupStatus()
    }
}