import 'package:flutter/material.dart';

class NoOverscrollSingleChildScrollView extends StatelessWidget {
  const NoOverscrollSingleChildScrollView({
    super.key,
    required this.child,
    this.padding,
  });

  final Widget child;
  final EdgeInsets? padding;

  @override
  Widget build(BuildContext context) {
    return NotificationListener<OverscrollIndicatorNotification>(
      onNotification: (overScroll) {
        overScroll.disallowIndicator();
        return true;
      },
      child: SingleChildScrollView(
        padding: padding,
        physics: const ClampingScrollPhysics(),
        child: child,
      ),
    );
  }
}
