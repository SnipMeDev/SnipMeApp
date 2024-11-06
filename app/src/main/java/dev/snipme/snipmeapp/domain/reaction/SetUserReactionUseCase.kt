package dev.snipme.snipmeapp.domain.reaction

import io.reactivex.Single
import dev.snipme.snipmeapp.domain.auth.AuthorizationUseCase
import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository
import dev.snipme.snipmeapp.domain.snippets.Snippet
import dev.snipme.snipmeapp.domain.user.GetSingleUserUseCase

class SetUserReactionUseCase(
    private val auth: AuthorizationUseCase,
    private val repository: SnippetRepository,
    private val getTargetReaction: GetTargetUserReactionUseCase,
    private val getSingleUser: GetSingleUserUseCase
) {
    operator fun invoke(snippet: Snippet, reaction: UserReaction): Single<Snippet> {
        val targetReaction = getTargetReaction(snippet, reaction)
        return auth()
            .andThen(getSingleUser())
            .flatMap { user ->
                repository.reaction(snippet.uuid, user.id, targetReaction)
                    .andThen(repository.snippet(snippet.uuid, user.id))
                    .doOnSuccess { repository.updateListener.onNext(it) }
            }
    }
}