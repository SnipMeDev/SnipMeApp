import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/model/main_model.dart';
import 'package:flutter_module/presentation/styles/color_styles.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/styles/surface_styles.dart';
import 'package:flutter_module/presentation/styles/text_styles.dart';
import 'package:flutter_module/presentation/widgets/code_text_view.dart';
import 'package:flutter_module/presentation/widgets/snippet_details_bar.dart';

class SnippetListTile extends HookWidget {
  const SnippetListTile({
    Key? key,
    required this.snippet,
    required this.onTap,
  }) : super(key: key);

  final bool isExpanded = true;
  final Snippet snippet;
  final GestureTapCallback? onTap;

  @override
  Widget build(BuildContext context) {
    return SurfaceStyles.snippetCard(
      onTap: onTap,
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
