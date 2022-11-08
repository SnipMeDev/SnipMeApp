import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_module/model/main_model.dart';
import 'package:flutter_module/presentation/styles/surface_styles.dart';
import 'package:flutter_module/presentation/widgets/state_icon.dart';

class SnippetActionBar extends StatelessWidget {
  const SnippetActionBar({
    Key? key,
    required this.snippet,
  }) : super(key: key);

  final Snippet snippet;

  @override
  Widget build(BuildContext context) {
    return SurfaceStyles.actionCard(
      child: Row(
        children: [
          StateIcon(
            icon: Icons.thumb_up_alt_outlined,
            active: snippet.isLiked,
          ),
          StateIcon(
            icon: Icons.thumb_down_alt_outlined,
            active: snippet.isDisliked,
          ),
          StateIcon(
            icon: Icons.save_alt_outlined,
            active: snippet.isSaved,
          ),
          const StateIcon(
            icon: Icons.copy_all_outlined,
          ),
          const StateIcon(
            icon: Icons.share,
          ),
        ],
      ),
    );
  }
}
