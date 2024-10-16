package dev.snipme.snipmeapp.domain.repository.snippet

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import dev.snipme.snipmeapp.domain.reaction.UserReaction
import dev.snipme.snipmeapp.domain.snippets.Snippet
import dev.snipme.snipmeapp.domain.snippets.SnippetScope
import dev.snipme.snipmeapp.domain.snippets.SnippetVisibility

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