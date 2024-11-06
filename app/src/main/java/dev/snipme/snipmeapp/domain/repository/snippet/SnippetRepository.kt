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

    fun snippets(userId: Int): Single<List<Snippet>>

    fun snippet(uuid: String, userId: Int): Single<Snippet>

    fun create(
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility,
        userId: Int
    ): Single<Snippet>

    fun update(
        uuid: String,
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility,
        userId: Int
    ): Single<Snippet>

    fun count(): Single<Int>

    fun reaction(uuid: String, userId: Int, reaction: UserReaction): Completable

    fun delete(uuid: String): Completable
}