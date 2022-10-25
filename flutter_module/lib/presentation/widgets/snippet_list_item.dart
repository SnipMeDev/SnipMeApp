import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/messages.dart';
import 'package:flutter_module/presentation/styles/color_styles.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/styles/text_styles.dart';
import 'package:flutter_module/presentation/widgets/code_text_view.dart';
import 'package:flutter_module/utils/extensions/text_extensions.dart';

class SnippetListTile extends HookWidget {
  const SnippetListTile({
    Key? key,
    required this.snippet,
  }) : super(key: key);

  final bool isExpanded = true;
  final Snippet snippet;

  @override
  Widget build(BuildContext context) {
    // TODO Export surface
    return Card(
      color: ColorStyles.surfacePrimary(),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(Dimens.m),
      ),
      child: InkWell(
        borderRadius: BorderRadius.circular(Dimens.m),
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
                  bottom: Dimens.m
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
      ),
    );
  }
}

class SnippetDetailsBar extends StatelessWidget {
  const SnippetDetailsBar({Key? key, required this.snippet}) : super(key: key);

  final Snippet snippet;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Column(
          children: [
            TextStyles.regular(snippet.language?.raw ?? ""),
            const SizedBox(height: Dimens.m),
            TextStyles.regular(snippet.owner?.login ?? ""),
            TextStyles.helper(snippet.modifiedAt?.toElapsedTime() ?? "")
          ],
        ),
      ],
    );
  }
}
