package dev.snipme.snipmeapp.domain.network

import io.reactivex.Completable
import dev.snipme.snipmeapp.domain.error.exception.NetworkNotAvailableException
import dev.snipme.snipmeapp.domain.repository.networkstate.NetworkStateRepository

class CheckNetworkAvailableUseCase(private val networkStateRepository: NetworkStateRepository) {

    operator fun invoke(): Completable =
        networkStateRepository.isAvailable()
            .flatMapCompletable { available ->
                if (available) {
                    Completable.complete()
                } else {
                    Completable.error(NetworkNotAvailableException())
                }
            }
}