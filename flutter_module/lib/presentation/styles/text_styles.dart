import 'package:flutter/material.dart';

class TextStyles extends Text {
  final String text;

  const TextStyles.title(this.text, {Key? key})
      : super(
          text,
          key: key,
          style: const TextStyle(fontSize: 16),
        );

  const TextStyles.code(this.text, {Key? key}) : super(
          text,
          key: key,
          style: const TextStyle(
            fontSize: 12,
            fontStyle: FontStyle.italic,
          ),
        );

  const TextStyles.regular(this.text, {Key? key})
      : super(
          text,
          key: key,
          style: const TextStyle(),
        );

  TextStyles.secondary(this.text, {Key? key})
      : super(
          text,
          key: key,
          style: TextStyle(color: Colors.black.withOpacity(0.5)),
        );

  TextStyles.label(this.text, {Key? key})
      : super(
          text,
          key: key,
          style: TextStyle(
            fontSize: 12,
            color: Colors.black.withOpacity(0.5),
          ),
        );

  TextStyles.helper(this.text, {Key? key})
      : super(
          text,
          key: key,
          style: TextStyle(
            fontSize: 12,
            color: Colors.black.withOpacity(0.5),
          ),
        );
}
