package dev.snipme.snipmeapp.domain.repository.snippet

import dev.snipme.snipmeapp.domain.snippets.Snippet
import dev.snipme.snipmeapp.domain.snippets.SnippetVisibility
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface SnippetRepository {

    val updateListener: BehaviorSubject<Snippet>

    fun getDemoSetupStatus(): Boolean

    fun setDemoSetupStatus(status: Boolean): Completable

    fun snippets(): Single<List<Snippet>>

    fun snippet(uuid: String): Single<Snippet>

    fun create(
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility,
        favorite: Boolean
    ): Single<Snippet>

    fun update(
        uuid: String,
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility,
        favorite: Boolean
    ): Single<Snippet>

    fun count(): Single<Int>

    fun delete(uuid: String): Completable
}