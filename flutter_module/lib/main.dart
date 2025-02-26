import 'package:flutter/material.dart';
import 'package:flutter_module/generated/data_model.g.dart';
import 'package:flutter_module/presentation/navigation/details/details_navigator.dart';
import 'package:flutter_module/presentation/navigation/login/login_navigator.dart';
import 'package:flutter_module/presentation/screens//main_screen.dart';
import 'package:flutter_module/presentation/screens/details_screen.dart';
import 'package:flutter_module/presentation/screens/login_screen.dart';
import 'package:flutter_module/utils/extensions/text_extensions.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router_plus/go_router_plus.dart';

void main() => runApp(ProviderScope(child: MyApp()));

class MyApp extends StatelessWidget {
  MyApp({super.key});

  final loginModel = ChannelLoginModel();
  final mainModel = ChannelMainModel();
  final detailModel = ChannelDetailModel();

  @override
  Widget build(BuildContext context) {
    final loginNavigator = LoginNavigator();
    final detailsNavigator = DetailsNavigator();
    final router = createGoRouter(
      screens: [
        LoginScreen(
          navigator: loginNavigator,
          model: loginModel,
        ),
        MainScreen(
          loginNavigator: loginNavigator,
          detailsNavigator: detailsNavigator,
          model: mainModel,
        ),
        DetailsScreen(
          navigator: detailsNavigator,
          model: detailModel,
        )
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
      theme: ThemeData(
        primarySwatch: Colors.blue,
        useMaterial3: false,
      ),
      // TODO Use theme tailor
      routeInformationProvider: router.routeInformationProvider,
      routeInformationParser: router.routeInformationParser,
      routerDelegate: router.routerDelegate,
    );
  }
}
