
import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/generated/assets.dart';
import 'package:flutter_module/model/main_model.dart';
import 'package:flutter_module/presentation/navigation/login/login_navigator.dart';
import 'package:flutter_module/presentation/screens/named_screen.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/styles/padding_styles.dart';
import 'package:flutter_module/presentation/styles/text_styles.dart';
import 'package:flutter_module/presentation/widgets/login_input_card.dart';
import 'package:flutter_module/presentation/widgets/no_overscroll_single_child_scroll_view.dart';
import 'package:flutter_module/presentation/widgets/rounded_action_button.dart';
import 'package:flutter_module/presentation/widgets/view_state_wrapper.dart';
import 'package:flutter_module/utils/extensions/state_extensions.dart';
import 'package:flutter_module/utils/hooks/use_navigator.dart';
import 'package:flutter_module/utils/hooks/use_observable_state_hook.dart';
import 'package:go_router_plus/go_router_plus.dart';

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

class _MainPage extends HookWidget {
  const _MainPage({
    required this.navigator,
    required this.model,
  });

  final LoginNavigator navigator;
  final LoginModelBridge model;

  @override
  Widget build(BuildContext context) {
    useNavigator([navigator]);

    final email = useState('');
    final password = useState('');
    final validationCorrect = useState(false);

    final state = useObservableState(
      LoginModelStateData(),
      () => model.getState(),
      (current, newState) => (current as LoginModelStateData).equals(newState),
    ).value;

    final event = useObservableState(
      LoginModelEventData(),
      () => model.getEvent(),
      (current, newState) => (current as LoginModelEventData).equals(newState),
    ).value;

    useEffect(() {
      model.checkLoginState();
      return null;
    }, []);

    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (event.event == LoginModelEvent.logged) {
        model.resetEvent();
        navigator.login();
      }
    });

    return Scaffold(
      body: SafeArea(
        child: ViewStateWrapper(
          isLoading: state.state == ModelState.loading,
          data: state.state,
          builder: (BuildContext context, _) {
            return NoOverscrollSingleChildScrollView(
              child: Expanded(
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
              ),
            );
          },
        ),
      ),
    );
  }
}
