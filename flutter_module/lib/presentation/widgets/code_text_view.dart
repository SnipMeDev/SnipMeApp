import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_module/model/main_model.dart';
import 'package:flutter_module/presentation/styles/text_styles.dart';
import 'package:flutter_module/utils/extensions/collection_extensions.dart';
import 'package:flutter_module/utils/extensions/text_extensions.dart';

class TextSelectionOptions {
  ToolbarOptions toolbarOptions;
  bool showCursor;

  TextSelectionOptions({
    required this.toolbarOptions,
    required this.showCursor,
  });

  TextSelectionOptions.copyable()
      : toolbarOptions = const ToolbarOptions(
          copy: true,
          cut: true,
          selectAll: true,
        ),
        showCursor = true;
}

class CodeTextView extends StatelessWidget {
  const CodeTextView({
    super.key,
    required this.code,
    this.maxLines,
    this.tokens,
    this.options,
    this.onTap,
  });

  final splitter = const LineSplitter();
  final String code;
  final int? maxLines;
  final List<SyntaxToken?>? tokens;
  final TextSelectionOptions? options;
  final GestureTapCallback? onTap;

  const CodeTextView.preview({
    super.key,
    required this.code,
    this.tokens,
    this.options,
    this.onTap,
  }) : maxLines = 5;

  @override
  Widget build(BuildContext context) {
    final maxLinesOrAll = maxLines ?? splitter.convert(code).length;

    return FutureBuilder(
      initialData: const <TextSpan>[],
      future: tokens.toSpans(
        code.lines(maxLinesOrAll),
        TextStyles.code(code).style!,
      ),
      builder: (_, value) {
        return SelectableText.rich(
          TextSpan(children: value.requireData),
          minLines: 1,
          maxLines: maxLinesOrAll,
          onTap: () {},
          toolbarOptions: options?.toolbarOptions,
          showCursor: options?.showCursor ?? false,
          enableInteractiveSelection: false,
          scrollPhysics: const NeverScrollableScrollPhysics(),
        );
      },
    );
  }
}
