import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/presentation/navigation/login/login_navigator.dart';
import 'package:flutter_module/presentation/screens/named_screen.dart';
import 'package:flutter_module/presentation/styles/padding_styles.dart';
import 'package:flutter_module/presentation/widgets/login_input_card.dart';
import 'package:flutter_module/utils/hooks/use_navigator.dart';
import 'package:go_router/go_router.dart';
import 'package:go_router_plus/go_router_plus.dart';

class LoginScreen extends NamedScreen implements InitialScreen, GuestScreen {
  LoginScreen(this.navigator) : super(name);

  static String name = 'login';
  final LoginNavigator navigator;

  @override
  Widget builder(BuildContext context, GoRouterState state) {
    return _MainPage(navigator: navigator);
  }
}

class _MainPage extends HookWidget {
  const _MainPage({Key? key, required this.navigator}) : super(key: key);

  final LoginNavigator navigator;

  @override
  Widget build(BuildContext context) {
    useNavigator([navigator]);

    final login = useState("");
    final password = useState("");

    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            // TODO Extract strings
            Text(LoginScreen.name),
            PaddingStyles.regular(
              LoginInputCard(
                onLoginChanged: (value) => login.value = value,
                onPasswordChanged: (value) => password.value = value,
              ),
            ),
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
