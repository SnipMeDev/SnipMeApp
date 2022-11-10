import 'package:flutter/material.dart';
import 'package:flutter_module/presentation/styles/color_styles.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';

class StateIcon extends StatelessWidget {
  const StateIcon({
    Key? key,
    required this.icon,
    this.active,
    this.onTap,
  }) : super(key: key);

  final IconData icon;
  final bool? active;
  final GestureTapCallback? onTap;

  @override
  Widget build(BuildContext context) {
    final color = getColorByState(active);
    return SizedBox(
      width: 24,
      height: 24,
      child: IconButton(
        padding: EdgeInsets.zero,
        splashRadius: Dimens.xl,
        icon: Icon(icon),
        color: color,
        onPressed: onTap,
      ),
    );
  }

  Color getColorByState(bool? active) {
    if (active == null) return Colors.black;
    return active ? ColorStyles.accent() : Colors.grey;
  }
}
