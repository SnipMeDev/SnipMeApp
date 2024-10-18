import 'package:flutter/material.dart';
import 'package:flutter_module/presentation/styles/color_styles.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/styles/padding_styles.dart';
import 'package:flutter_module/presentation/styles/surface_styles.dart';
import 'package:flutter_module/presentation/styles/text_styles.dart';

class RoundedActionButton extends StatelessWidget {
  const RoundedActionButton({
    super.key,
    required this.icon,
    required this.title,
    this.enabled = true,
    this.onPressed,
  });

  final IconData icon;
  final String title;
  final bool enabled;
  final GestureTapCallback? onPressed;

  @override
  Widget build(BuildContext context) {
    return SurfaceStyles.roundedFloatingCard(
      onTap: enabled ? () => onPressed?.call() : null,
      child: PaddingStyles.small(
        Opacity(
          opacity: enabled ? 1.0 : 0.3,
          child: Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              const SizedBox(width: Dimens.m),
              Icon(icon, color: ColorStyles.accent()),
              const SizedBox(width: Dimens.m),
              TextStyles.regular(
                title.toUpperCase(),
                color: ColorStyles.accent(),
              ),
              const SizedBox(width: Dimens.m),
            ],
          ),
        ),
      ),
    );
  }
}
