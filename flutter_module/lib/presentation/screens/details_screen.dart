import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/model/main_model.dart';
import 'package:flutter_module/presentation/navigation/details/details_navigator.dart';
import 'package:flutter_module/presentation/screens/named_screen.dart';
import 'package:flutter_module/presentation/widgets/snippet_action_bar.dart';
import 'package:flutter_module/utils/hooks/use_navigator.dart';
import 'package:go_router/go_router.dart';

class DetailsScreen extends NamedScreen {
  DetailsScreen(this.navigator) : super(name);

  static String name = 'details';

  final DetailsNavigator navigator;

  @override
  Widget builder(BuildContext context, GoRouterState state) {
    return _DetailsPage(navigator: navigator, snippet: navigator.snippet);
  }
}

class _DetailsPage extends HookWidget {
  const _DetailsPage({
    Key? key,
    required this.navigator,
    required this.snippet,
  }) : super(key: key);

  final DetailsNavigator navigator;
  final Snippet? snippet;

  @override
  Widget build(BuildContext context) {
    useNavigator([navigator]);

    // TODO Add view state wrapper and show error for null snippet
    return Scaffold(
      appBar: AppBar(
        leading: BackButton(onPressed: navigator.back),
      ),
      body: Center(
        child: SnippetActionBar(
          snippet: snippet!,
          onLikeTap: () {},
          onDislikeTap: () {},
          onSaveTap: () {},
          onCopyTap: () {},
          onShareTap: () {},
        ),
      ),
    );
  }
}
