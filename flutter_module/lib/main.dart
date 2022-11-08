import 'package:flutter/material.dart';
import 'package:flutter_module/model/main_model.dart';
import 'package:flutter_module/presentation/navigation/details/details_navigator.dart';
import 'package:flutter_module/presentation/navigation/login/login_navigator.dart';
import 'package:flutter_module/presentation/screens//main_screen.dart';
import 'package:flutter_module/presentation/screens/details_screen.dart';
import 'package:flutter_module/presentation/screens/login_screen.dart';
import 'package:flutter_module/utils/extensions/text_extensions.dart';
import 'package:go_router_plus/go_router_plus.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  MyApp({Key? key}) : super(key: key);

  final mainModel = MainModelBridge();

  @override
  Widget build(BuildContext context) {
    final loginNavigator = LoginNavigator();
    final detailsNavigator = DetailsNavigator();
    final router = createGoRouter(
      screens: [
        LoginScreen(loginNavigator),
        MainScreen(
          loginNavigator: loginNavigator,
          detailsNavigator: detailsNavigator,
          model: mainModel,
        ),
        DetailsScreen(detailsNavigator)
      ],
      redirectors: [
        ScreenRedirector(),
        AuthRedirector(
          state: loginNavigator,
          guestRedirectPath: LoginScreen.name.route,
          userRedirectPath: MainScreen.name.route,
        )
      ],
      refreshNotifiers: [loginNavigator],
    );

    return MaterialApp.router(
      title: 'SnipMe',
      theme: ThemeData(primarySwatch: Colors.blue),
      // TODO Use theme tailor
      routeInformationProvider: router.routeInformationProvider,
      routeInformationParser: router.routeInformationParser,
      routerDelegate: router.routerDelegate,
    );
  }
}
