import 'package:flutter/material.dart';
import 'package:flutter_module/presentation/navigation/login/login_navigator.dart';
import 'package:flutter_module/presentation/screens/named_screen.dart';
import 'package:go_router/go_router.dart';
import 'package:go_router_plus/go_router_plus.dart';

class LoginScreen extends NamedScreen implements InitialScreen, GuestScreen {
  LoginScreen(this.navigator) : super(name);

  static String name = 'login';
  final LoginNavigator navigator;

  @override
  Widget builder(BuildContext context, GoRouterState state) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            // TODO Extract strings
            const Text("Login"),
            MaterialButton(
              child: const Text("Navigate to login"),
              onPressed: navigator.login,
            ),
          ],
        ),
      ),
    );
  }
}
