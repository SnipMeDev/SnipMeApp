package dev.snipme.snipmeapp.util.extension

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.Disposable

fun Disposable?.inProgress() = (this != null && !isDisposed)

fun <T> Single<T>.mapError(mapper: (Throwable) -> Throwable) =
    this.onErrorResumeNext { Single.error(mapper(it)) }

fun Completable.mapError(mapper: (Throwable) -> Throwable) =
    this.onErrorResumeNext { Completable.error(mapper(it)) }

fun <T, E> Single<List<T>>.mapItems(mapper: (T) -> E): Single<List<E>> =
    this.map { items -> items.map { mapper(it) } }

fun <T> Single<List<T>>.filterItems(condition: (T) -> Boolean): Single<List<T>> =
    this.map { items -> items.filter { condition(it) } }