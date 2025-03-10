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
    return Row(
      children: [
        Expanded(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              TextStyles.regular(snippet.language?.raw ?? "Unknown language"),
              const SizedBox(height: Dimens.m),
              // TODO FIX
              const TextStyles.secondary("!!!Show archive status!!!"),
              const SizedBox(height: Dimens.s),
              TextStyles.helper(snippet.timeAgo ?? "")
            ],
          ),
        ),
        SurfaceStyles.rateBox(
          TextStyles.title(
            TextSpan(
              children: [
                WidgetSpan(child: Icon(Icons.visibility)),
              ]
            ).text!
          ),
        )
      ],
    );
  }
}
