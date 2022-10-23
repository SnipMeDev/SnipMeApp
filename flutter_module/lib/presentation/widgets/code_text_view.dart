import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_module/messages.dart';
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
    Key? key,
    required this.code,
    this.maxLines,
    this.tokens,
    this.options,
    this.onTap,
  }) : super(key: key);

  final splitter = const LineSplitter();
  final String code;
  final int? maxLines;
  final List<SyntaxToken?>? tokens;
  final TextSelectionOptions? options;
  final GestureTapCallback? onTap;

  const CodeTextView.preview({
    Key? key,
    required this.code,
    this.tokens,
    this.options,
    this.onTap,
  })  : maxLines = 5,
        super(key: key);

  @override
  Widget build(BuildContext context) {
    final maxLinesOrAll = maxLines ?? splitter.convert(code).length;

    return SelectableText.rich(
      TextSpan(
        style: const TextStyle(color: Colors.black),
        children: tokens.toSpans(
          code.lines(maxLinesOrAll),
          const TextStyle(color: Colors.black),
        ),
      ),
      minLines: 1,
      maxLines: maxLinesOrAll,
      onTap: onTap,
      toolbarOptions: options?.toolbarOptions,
      showCursor: options?.showCursor ?? false,
    );
  }
}
