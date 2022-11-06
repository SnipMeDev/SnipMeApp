import 'dart:async';

import 'package:flutter/widgets.dart';
import 'package:flutter_module/presentation/navigation/details/details_navigator.dart';
import 'package:flutter_module/presentation/screens/details_screen.dart';
import 'package:go_router/go_router.dart';
import 'package:go_router_plus/go_router_plus.dart';

class DetailsRedirector extends ScreenRedirector {
  DetailsRedirector(this.navigator);

  final DetailsNavigator navigator;

  @override
  FutureOr<String?> redirect(
    Screen screen,
    BuildContext context,
    GoRouterState state,
  ) {
    if (navigator.snippetId != null) {
      return '/${DetailsScreen.name}';
    }

    return null;
  }

  @override
  bool shouldRedirect(Screen screen) => true;
}
