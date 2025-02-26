import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/generated/assets.dart';
import 'package:flutter_module/model/main_model.dart';
import 'package:flutter_module/presentation/navigation/login/login_navigator.dart';
import 'package:flutter_module/presentation/providers/login_page_state_provider.dart';
import 'package:flutter_module/presentation/screens/named_screen.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/styles/padding_styles.dart';
import 'package:flutter_module/presentation/styles/text_styles.dart';
import 'package:flutter_module/presentation/widgets/login_input_card.dart';
import 'package:flutter_module/presentation/widgets/no_overscroll_single_child_scroll_view.dart';
import 'package:flutter_module/presentation/widgets/rounded_action_button.dart';
import 'package:flutter_module/presentation/widgets/view_state_wrapper.dart';
import 'package:flutter_module/utils/hooks/use_navigator.dart';
import 'package:go_router_plus/go_router_plus.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';

class LoginScreen extends NamedScreen implements InitialScreen, GuestScreen {
  LoginScreen({
    required this.navigator,
    required this.model,
  }) : super(name);

  static String name = 'login';
  final LoginNavigator navigator;
  final LoginModelBridge model;

  @override
  Widget build(BuildContext context, GoRouterState state) {
    return _MainPage(
      navigator: navigator,
      model: model,
    );
  }
}

class _MainPage extends HookConsumerWidget {
  const _MainPage({
    required this.navigator,
    required this.model,
  });

  final LoginNavigator navigator;
  final LoginModelBridge model;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    useNavigator([navigator]);

    final email = useState('mail@o2.pl');
    final password = useState('12345678');
    final validationCorrect = useState(true);

    final stateNotification = ref.watch(loginPageStateProvider);
    final eventNotification = ref.watch(loginPageEventProvider);
    final event = eventNotification.event;

    useEffect(() {
      model.checkLoginState();
      return null;
    }, []);

    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (event == LoginModelEvent.logged) {
        model.resetEvent();
        navigator.login();
      }
    });

    return Scaffold(
      body: SafeArea(
        child: ViewStateWrapper(
          isLoading: stateNotification.state == ModelState.loading,
          data: stateNotification,
          builder: (BuildContext context, _) {
            return NoOverscrollSingleChildScrollView(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const SizedBox(height: Dimens.xxl),
                  TextStyles.appLogo('SnipMe'),
                  const SizedBox(height: Dimens.xxl),
                  Image.asset(Assets.appLogo),
                  const SizedBox(height: Dimens.xxl),
                  const TextStyles.secondary('Snip your favorite code'),
                  PaddingStyles.regular(
                    LoginInputCard(
                      emailValue: email.value,
                      passwordValue: password.value,
                      onEmailChanged: (emailValue) {
                        email.value = emailValue;
                      },
                      onPasswordChanged: (passwordValue) {
                        password.value = passwordValue;
                      },
                      onValidChanged: (isValid) {
                        validationCorrect.value = isValid;
                      },
                    ),
                  ),
                  Center(
                    child: RoundedActionButton(
                      icon: Icons.check_circle,
                      title: 'Login',
                      enabled: validationCorrect.value,
                      onPressed: () {
                        model.loginOrRegister(email.value, password.value);
                      },
                    ),
                  ),
                  const SizedBox(height: Dimens.xxl),
                ],
              ),
            );
          },
        ),
      ),
    );
  }
}
