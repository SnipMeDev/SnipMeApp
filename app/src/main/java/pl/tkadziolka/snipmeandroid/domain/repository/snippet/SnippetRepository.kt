package pl.tkadziolka.snipmeandroid.domain.repository.snippet

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import pl.tkadziolka.snipmeandroid.domain.reaction.UserReaction
import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet
import pl.tkadziolka.snipmeandroid.domain.snippets.SnippetScope
import pl.tkadziolka.snipmeandroid.domain.snippets.SnippetVisibility

interface SnippetRepository {

    val updateListener: BehaviorSubject<Snippet>

    fun snippets(scope: SnippetScope, page: Int): Single<List<Snippet>>

    fun snippet(id: String): Single<Snippet>

    fun create(
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility
    ): Single<Snippet>

    fun update(
        uuid: String,
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility
    ): Single<Snippet>

    fun count(scope: SnippetScope): Single<Int>

    fun reaction(uuid: String, reaction: UserReaction): Completable

    fun delete(uuid: String): Completable
}