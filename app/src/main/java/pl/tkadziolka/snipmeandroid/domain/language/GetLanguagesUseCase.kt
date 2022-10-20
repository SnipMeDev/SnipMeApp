package pl.tkadziolka.snipmeandroid.domain.language

import pl.tkadziolka.snipmeandroid.domain.auth.AuthorizationUseCase
import pl.tkadziolka.snipmeandroid.domain.network.CheckNetworkAvailableUseCase
import pl.tkadziolka.snipmeandroid.domain.repository.language.LanguageRepository
import pl.tkadziolka.snipmeandroid.util.extension.mapItems

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