import 'package:equatable/equatable.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter_module/model/main_model.dart';
import 'package:flutter_module/presentation/styles/text_styles.dart';
import 'package:flutter_module/utils/extensions/text_extensions.dart';
import 'package:collection/collection.dart';

class TokenSpan with EquatableMixin {
  String value;
  Color color;
  int start;
  int end;

  TokenSpan({
    required this.value,
    required this.color,
    required this.start,
    required this.end,
  });

  @override
  List<Object?> get props => [value, color, start, end];
}

extension SyntaxSpanExtension on List<SyntaxToken?>? {
  List<TextSpan> toSpans(String text, TextStyle baseStyle) {
    if (this == null) return [TextSpan(text: text, style: baseStyle)];
    if (this!.isEmpty) return [TextSpan(text: text, style: baseStyle)];

    final syntaxTokens = this!.whereType<SyntaxToken>();

    final tokens = syntaxTokens.map(
      (token) => TokenSpan(
        value: text.substring(token.start!, token.end!),
        color: Color(token.color!),
        start: token.start!,
        end: token.end!,
      ),
    );

    final uniqueTokens = tokens.where((tested) {
      final isDuplicated = tokens.any(
        (span) =>
            tested != span &&
            span.value.contains(tested.value) &&
            tested.start >= span.start &&
            tested.end <= span.end,
      );

      return !isDuplicated;
    });

    final tokenIndices =
        uniqueTokens.expand((token) => [token.start, token.end]).toList();

    final phrases = text.splitByIndices(tokenIndices);

    return phrases.map((phrase) {
      TextStyle style = baseStyle;

      final foundToken = uniqueTokens.firstWhereOrNull(
        (span) => span.value == phrase,
      );

      if (foundToken != null) {
        style = TextStyles.code(text).style!.copyWith(color: foundToken.color);
      }

      return TextSpan(text: phrase, style: style);
    }).toList();
  }
}
