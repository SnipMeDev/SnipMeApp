import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_module/presentation/styles/surface_styles.dart';

class SnippetActionBar extends StatelessWidget {
  const SnippetActionBar({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SurfaceStyles.actionCard(
      child: Row(
        children: const [
          Icon(Icons.thumb_up_alt_outlined),
          Icon(Icons.thumb_down_alt_outlined),
          Icon(Icons.save_alt_outlined),
          Icon(Icons.copy_all_outlined),
          Icon(Icons.share),
        ],
      ),
    );
  }
}
