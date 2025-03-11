import 'package:flutter/material.dart';
import 'package:flutter_module/generated/data_model.g.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/styles/surface_styles.dart';
import 'package:flutter_module/presentation/styles/text_styles.dart';

class SnippetDetailsBar extends StatelessWidget {
  const SnippetDetailsBar({
    required this.snippet,
    super.key,
  });

  final Snippet snippet;

  @override
  Widget build(BuildContext context) {
    final visibilityIcon = snippet.isHidden == true
        ? Icons.visibility_off_outlined
        : Icons.visibility_outlined;

    final visibilityText = snippet.isHidden == true ? "Hidden" : "Visible";

    return Row(children: [
      Expanded(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            TextStyles.regular(snippet.language?.raw ?? "Unknown language"),
            const SizedBox(height: Dimens.m),
            TextStyles.secondary(visibilityText),
            const SizedBox(height: Dimens.s),
            TextStyles.helper(snippet.timeAgo ?? "")
          ],
        ),
      ),
      SurfaceStyles.rateBox(Text.rich(
        TextSpan(children: [WidgetSpan(child: Icon(visibilityIcon))]),
      )),
    ]);
  }
}
