package pl.tkadziolka.snipmeandroid.domain.reaction

import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet

class GetTargetUserReactionUseCase {
    operator fun invoke(snippet: Snippet, reaction: UserReaction) =
        when {
            snippet.userReaction == reaction -> UserReaction.NONE
            else -> reaction
        }
}