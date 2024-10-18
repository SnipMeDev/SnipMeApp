import 'package:flutter/material.dart';
import 'package:flutter_module/generated/assets.dart';
import 'package:flutter_module/model/main_model.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/styles/surface_styles.dart';
import 'package:flutter_module/presentation/styles/text_styles.dart';

class SnippetDetailsBar extends StatelessWidget {
  const SnippetDetailsBar({
    super.key,
    required this.snippet,
  });

  final Snippet snippet;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Expanded(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              TextStyles.regular(snippet.language?.raw ?? "Unknown language"),
              const SizedBox(height: Dimens.m),
              snippet.isOwner == true
                  ? TextStyles.secondaryBold(snippet.owner?.login ?? "")
                  : TextStyles.secondary(snippet.owner?.login ?? ""),
              const SizedBox(height: Dimens.s),
              TextStyles.helper(snippet.timeAgo ?? "")
            ],
          ),
        ),
        _UserReactionIndicator(reaction: snippet.userReaction),
        const SizedBox(width: Dimens.l),
        SurfaceStyles.rateBox(
          TextStyles.title(
            _getVoteCountText(snippet.voteResult),
          ),
        )
      ],
    );
  }

  String _getVoteCountText(int? voteResult) {
    const defaultValue = '+0';
    if (voteResult == null) return defaultValue;
    if (voteResult == 0) return defaultValue;
    if (voteResult > 0) return '+$voteResult';
    return '-$voteResult';
  }
}

class _UserReactionIndicator extends StatelessWidget {
  const _UserReactionIndicator({
    this.reaction,
  });

  final UserReaction? reaction;

  @override
  Widget build(BuildContext context) {
    if (reaction == UserReaction.like) {
      return Image.asset(Assets.reactionLike);
    }

    if (reaction == UserReaction.dislike) {
      return Image.asset(Assets.reactionDislike);
    }

    return Image.asset(Assets.reactionUndefined);
  }
}
