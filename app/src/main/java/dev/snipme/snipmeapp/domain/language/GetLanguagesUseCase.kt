package dev.snipme.snipmeapp.domain.language

import dev.snipme.snipmeapp.domain.auth.AuthorizationUseCase
import dev.snipme.snipmeapp.domain.network.CheckNetworkAvailableUseCase
import dev.snipme.snipmeapp.domain.repository.language.LanguageRepository
import dev.snipme.snipmeapp.util.extension.mapItems

class GetLanguagesUseCase(
    private val auth: AuthorizationUseCase,
    private val networkState: CheckNetworkAvailableUseCase,
    private val language: LanguageRepository
) {
    operator fun invoke() =
        auth()
            .andThen(networkState())
            .andThen(language.languages().mapItems { it.name })
}