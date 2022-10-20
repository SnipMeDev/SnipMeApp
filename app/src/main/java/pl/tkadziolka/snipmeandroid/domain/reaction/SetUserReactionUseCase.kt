package pl.tkadziolka.snipmeandroid.domain.reaction

import io.reactivex.Single
import pl.tkadziolka.snipmeandroid.domain.auth.AuthorizationUseCase
import pl.tkadziolka.snipmeandroid.domain.repository.snippet.SnippetRepository
import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet

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