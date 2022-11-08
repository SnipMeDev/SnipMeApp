import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter_module/presentation/styles/color_styles.dart';

class StateIcon extends StatelessWidget {
  const StateIcon({
    Key? key,
    required this.icon,
    this.active,
  }) : super(key: key);

  final IconData icon;
  final bool? active;

  @override
  Widget build(BuildContext context) {
    final color = getColorByState(active);
    return Icon(icon, color: color);
  }

  Color getColorByState(bool? active) {
    if (active == null) return Colors.black;
    return active ? ColorStyles.accent() : Colors.grey;
  }
}
