import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/messages.dart';
import 'package:flutter_module/presentation/styles/color_styles.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/styles/surface_styles.dart';
import 'package:flutter_module/presentation/styles/text_styles.dart';
import 'package:flutter_module/presentation/widgets/code_text_view.dart';

class SnippetListTile extends HookWidget {
  const SnippetListTile({
    Key? key,
    required this.snippet,
  }) : super(key: key);

  final bool isExpanded = true;
  final Snippet snippet;

  @override
  Widget build(BuildContext context) {
    return SurfaceStyles.snippetCard(
      onTap: () {},
      child: Expanded(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Padding(
              padding: const EdgeInsets.only(
                top: Dimens.l,
                left: Dimens.l,
                right: Dimens.l,
                bottom: Dimens.m,
              ),
              child: TextStyles.title(snippet.title ?? ""),
            ),
            Ink(
              color: ColorStyles.codeBackground(),
              child: Padding(
                padding: const EdgeInsets.symmetric(
                  vertical: Dimens.m,
                  horizontal: Dimens.l,
                ),
                child: Expanded(
                  child: CodeTextView.preview(
                    code: snippet.code?.raw ?? "",
                    tokens: snippet.code?.tokens,
                  ),
                ),
              ),
            ),
            const SizedBox(height: Dimens.m),
            if (isExpanded) ...[
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: Dimens.l),
                child: SnippetDetailsBar(snippet: snippet),
              ),
              const SizedBox(height: Dimens.m),
            ],
          ],
        ),
      ),
    );
  }
}

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
              TextStyles.secondary(snippet.owner?.login ?? ""),
              const SizedBox(height: Dimens.s),
              TextStyles.helper(snippet.timeAgo ?? "")
            ],
          ),
        ),
        SurfaceStyles.rateBox(
          TextStyles.title(
            _getVoteCountText(snippet.voteResult),
          ),
        )
      ],
    );
  }

  String _getVoteCountText(int? voteResult) {
    if (voteResult == null) return '-';
    if (voteResult == 0) return '-';
    if (voteResult > 0) return '+$voteResult';
    return '-$voteResult';
  }
}
