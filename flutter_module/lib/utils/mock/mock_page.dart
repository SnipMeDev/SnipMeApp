import 'package:flutter/material.dart';
import 'package:flutter_module/presentation/styles/padding_styles.dart';

class MockPage extends StatelessWidget {
  const MockPage({
    Key? key,
    required this.children,
  }) : super(key: key);

  final List<Widget> children;

  @override
  Widget build(BuildContext context) {
    return ColoredBox(
      color: Colors.grey,
      child: PaddingStyles.regular(
        Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: children,
          ),
        ),
      ),
    );
  }
}
