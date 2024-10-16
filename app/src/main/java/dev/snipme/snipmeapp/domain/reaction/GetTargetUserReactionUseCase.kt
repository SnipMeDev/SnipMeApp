package dev.snipme.snipmeapp.domain.reaction

import dev.snipme.snipmeapp.domain.snippets.Snippet

class GetTargetUserReactionUseCase {
    operator fun invoke(snippet: Snippet, reaction: UserReaction) =
        when {
            snippet.userReaction == reaction -> UserReaction.NONE
            else -> reaction
        }
}