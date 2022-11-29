import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_module/model/main_model.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/styles/surface_styles.dart';
import 'package:flutter_module/presentation/widgets/state_icon.dart';

class SnippetActionBar extends StatelessWidget {
  const SnippetActionBar({
    Key? key,
    required this.snippet,
    this.onLikeTap,
    this.onDislikeTap,
    this.onSaveTap,
    this.onCopyTap,
    this.onShareTap,
  }) : super(key: key);

  final Snippet snippet;
  final GestureTapCallback? onLikeTap;
  final GestureTapCallback? onDislikeTap;
  final GestureTapCallback? onSaveTap;
  final GestureTapCallback? onCopyTap;
  final GestureTapCallback? onShareTap;

  @override
  Widget build(BuildContext context) {
    return SurfaceStyles.actionCard(
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          StateIcon(
            icon: Icons.thumb_up_alt_outlined,
            active: snippet.isLiked,
            onTap: snippet.isLiked == false ? null : onLikeTap,
          ),
          const SizedBox(width: Dimens.l),
          StateIcon(
            icon: Icons.thumb_down_alt_outlined,
            active: snippet.isDisliked,
            onTap: snippet.isDisliked == false ? null : onDislikeTap,
          ),
          const SizedBox(width: Dimens.l),
          StateIcon(
            icon: Icons.save_alt_outlined,
            active: snippet.isSaved,
            onTap: snippet.isSaved == false
                ? null
                : getSaveCallback(snippet.isSaved, onSaveTap),
          ),
          const SizedBox(width: Dimens.l),
          StateIcon(
            icon: Icons.copy_all_outlined,
            onTap: onCopyTap,
          ),
          const SizedBox(width: Dimens.l),
          StateIcon(
            icon: Icons.share,
            onTap: onShareTap,
          ),
        ],
      ),
    );
  }

  GestureTapCallback? getSaveCallback(
    bool? isSaved,
    GestureTapCallback? onSaveTap,
  ) {
    if (isSaved == null) return null;
    if (isSaved == true) return null;
    return onSaveTap;
  }
}