import 'package:flutter/material.dart';
import 'package:flutter_module/presentation/styles/color_styles.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';

class SurfaceStyles {

  static snippetCard({required Widget child, GestureTapCallback? onTap}) {
    return Card(
      color: ColorStyles.surfacePrimary(),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(Dimens.m),
      ),
      child: InkWell(
        borderRadius: BorderRadius.circular(Dimens.m),
        onTap: onTap,
        child: child,
      ),
    );
  }
}
