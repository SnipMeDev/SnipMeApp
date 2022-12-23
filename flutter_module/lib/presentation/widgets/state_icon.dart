import 'package:flutter/material.dart';
import 'package:flutter_module/presentation/styles/color_styles.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';

class StateIcon extends StatelessWidget {
  const StateIcon({
    Key? key,
    required this.icon,
    this.activeColor = Colors.black,
    this.active,
    this.onTap,
  }) : super(key: key);

  final IconData icon;
  final Color activeColor;
  final bool? active;
  final GestureTapCallback? onTap;

  @override
  Widget build(BuildContext context) {
    final color = getColorByState(active, activeColor);
    return SizedBox(
      width: 24,
      height: 24,
      child: IconButton(
        padding: EdgeInsets.zero,
        splashRadius: Dimens.xl,
        icon: Icon(icon),
        color: color,
        disabledColor: color,
        onPressed: onTap,
      ),
    );
  }

  Color getColorByState(bool? active, Color activeColor) {
    if (active == null) return activeColor;
    return active ? ColorStyles.accent() : Colors.grey;
  }
}
