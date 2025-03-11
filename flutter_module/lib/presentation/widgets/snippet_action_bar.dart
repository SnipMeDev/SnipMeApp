import 'package:flutter/material.dart';
import 'package:flutter_module/generated/data_model.g.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/styles/surface_styles.dart';
import 'package:flutter_module/presentation/widgets/state_icon.dart';

class SnippetActionBar extends StatelessWidget {
  const SnippetActionBar({
    required this.snippet,
    this.onFavoriteTap,
    this.onSaveTap,
    this.onCopyTap,
    this.onShareTap,
    this.onHideTap,
    this.onDeleteTap,
    super.key,
  });

  final Snippet snippet;
  final GestureTapCallback? onFavoriteTap;
  final GestureTapCallback? onSaveTap;
  final GestureTapCallback? onCopyTap;
  final GestureTapCallback? onShareTap;
  final GestureTapCallback? onHideTap;
  final GestureTapCallback? onDeleteTap;

  @override
  Widget build(BuildContext context) {
    return SurfaceStyles.actionCard(
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          StateIcon(
            icon: Icons.favorite,
            active: snippet.isFavorite,
            onTap: onFavoriteTap,
          ),
          const SizedBox(width: Dimens.l),
          StateIcon(
            icon: Icons.share,
            onTap: onShareTap,
          ),
          const SizedBox(width: Dimens.l),
          StateIcon(
            icon: Icons.save_alt_outlined,
            onTap: onSaveTap,
          ),
          const SizedBox(width: Dimens.l),
          StateIcon(
            icon: Icons.copy_all_outlined,
            onTap: onCopyTap,
          ),
          const SizedBox(width: Dimens.l),
          StateIcon(
            icon: Icons.visibility_off_outlined,
            onTap: onHideTap,
          ),
          const SizedBox(width: Dimens.l),
          StateIcon(
            onTap: onDeleteTap,
            active: true,
            activeColor: Colors.redAccent,
            icon: Icons.delete_outline_outlined,
          ),
        ],
      ),
    );
  }
}
