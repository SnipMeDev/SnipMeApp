package dev.snipme.snipmeapp.domain.repository.networkstate

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.Observable
import io.reactivex.Single

class NetworkStateRepositoryReal : NetworkStateRepository {
    override fun isAvailable(): Single<Boolean> =
        ReactiveNetwork.checkInternetConnectivity()

    override fun observe(): Observable<Boolean> =
        ReactiveNetwork.observeInternetConnectivity()
}