package pl.tkadziolka.snipmeandroid.domain.network

import io.reactivex.Completable
import pl.tkadziolka.snipmeandroid.domain.error.exception.NetworkNotAvailableException
import pl.tkadziolka.snipmeandroid.domain.repository.networkstate.NetworkStateRepository

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