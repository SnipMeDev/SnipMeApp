package dev.snipme.snipmeapp.domain.reaction

import io.reactivex.Single
import dev.snipme.snipmeapp.domain.auth.AuthorizationUseCase
import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository
import dev.snipme.snipmeapp.domain.snippets.Snippet

class SetUserReactionUseCase(
    private val auth: AuthorizationUseCase,
    private val repository: SnippetRepository,
    private val getTargetReaction: GetTargetUserReactionUseCase
) {
    operator fun invoke(snippet: Snippet, reaction: UserReaction): Single<Snippet> {
        val targetReaction = getTargetReaction(snippet, reaction)
        return auth()
            .andThen(repository.reaction(snippet.uuid, targetReaction))
            .andThen(repository.snippet(snippet.uuid))
            .doOnSuccess { repository.updateListener.onNext(it) }
    }
}