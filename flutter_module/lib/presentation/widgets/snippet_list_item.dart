import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/messages.dart';
import 'package:flutter_module/presentation/styles/color_styles.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/styles/padding_styles.dart';
import 'package:flutter_module/presentation/styles/text_styles.dart';
import 'package:flutter_module/presentation/widgets/code_text_view.dart';

class SnippetListTile extends HookWidget {
  const SnippetListTile({
    Key? key,
    required this.snippet,
  }) : super(key: key);

  final Snippet snippet;

  @override
  Widget build(BuildContext context) {
    return Card(
      color: ColorStyles.surfacePrimary(),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(Dimens.m),
      ),
      child: Column(
        children: [
          PaddingStyles.regular(
            TextStyles.title(snippet.title ?? ""),
          ),
          ColoredBox(
            color: ColorStyles.codeBackground(),
            child: Padding(
              padding: const EdgeInsets.symmetric(
                vertical: Dimens.m,
                horizontal: Dimens.l,
              ),
              child: CodeTextView.preview(
                code: snippet.code?.raw ?? "",
                tokens: snippet.code?.tokens,
              ),
            ),
          ),
          const SizedBox(height: Dimens.m),
          Row(children: [
            Column(),

          ],)
        ],
      ),
    );
  }
}
