import 'package:flutter/material.dart';
import 'package:flutter_module/presentation/navigation/details/details_navigator.dart';
import 'package:flutter_module/presentation/screens/named_screen.dart';
import 'package:go_router/go_router.dart';

class DetailsScreen extends NamedScreen {
  DetailsScreen(this.navigator) : super(name);

  static String name = 'details';

  final DetailsNavigator navigator;

  @override
  Widget builder(BuildContext context, GoRouterState state) {
    return WillPopScope(
      onWillPop: () async {
        // TODO Fix back action
        GoRouter.of(context).pop();
        return Future.value(false);
      },
      child: const Scaffold(
        body: Center(
          child: Text("Details"),
        ),
      ),
    );
  }
}
