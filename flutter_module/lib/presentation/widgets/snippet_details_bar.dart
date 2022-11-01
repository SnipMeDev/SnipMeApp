import 'package:flutter/material.dart';
import 'package:flutter_module/model/main_model.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/styles/surface_styles.dart';
import 'package:flutter_module/presentation/styles/text_styles.dart';

class SnippetDetailsBar extends StatelessWidget {
  const SnippetDetailsBar({
    Key? key,
    required this.snippet,
  }) : super(key: key);

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
              TextStyles.regular(snippet.language?.raw ?? ""),
              const SizedBox(height: Dimens.m),
              snippet.isOwner == true
                  ? TextStyles.secondaryBold(snippet.owner?.login ?? "")
                  : TextStyles.secondary(snippet.owner?.login ?? ""),
              const SizedBox(height: Dimens.s),
              TextStyles.helper(snippet.timeAgo ?? "")
            ],
          ),
        ),
        _VoteIndicator(reaction: snippet.userReaction),
        const SizedBox(width: Dimens.m),
        SurfaceStyles.rateBox(
          TextStyles.title(
            _getVoteCountText(snippet.voteResult),
          ),
        )
      ],
    );
  }

  String _getVoteCountText(int? voteResult) {
    if (voteResult == null) return '--';
    if (voteResult == 0) return '--';
    if (voteResult > 0) return '+$voteResult';
    return '-$voteResult';
  }
}

class _VoteIndicator extends StatelessWidget {
  const _VoteIndicator({
    Key? key,
    this.reaction,
  }) : super(key: key);

  final UserReaction? reaction;

  @override
  Widget build(BuildContext context) => Image.asset('');
  // TODO Correct generating asset paths from plugin
}
