import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/styles/padding_styles.dart';
import 'package:flutter_module/presentation/styles/surface_styles.dart';
import 'package:flutter_module/presentation/widgets/text_input_field.dart';
import 'package:flutter_module/utils/hooks/use_same_state.dart';
import 'package:validators/validators.dart';

const _minPasswordLength = 8;

class LoginInputCard extends HookWidget {
  const LoginInputCard({
    Key? key,
    required this.onEmailChanged,
    required this.onPasswordChanged,
    this.onValidChanged,
  }) : super(key: key);

  final TextInputCallback onEmailChanged;
  final TextInputCallback onPasswordChanged;
  final Function(bool)? onValidChanged;

  @override
  Widget build(BuildContext context) {
    final emailErrorTextState = useSameState<String?>('');
    final passwordErrorTextState = useSameState<String?>('');

    return SurfaceStyles.snippetCard(
      child: PaddingStyles.regular(
        Column(
          children: [
            const SizedBox(height: Dimens.l),
            TextInputField(
              label: 'Email',
              onChanged: onEmailChanged,
              validator: (input) => _validate(
                emailErrorTextState,
                passwordErrorTextState,
                email: input as String,
              ),
            ),
            const SizedBox(height: Dimens.xl),
            TextInputField(
              label: 'Password',
              onChanged: onPasswordChanged,
              isPassword: true,
              validator: (input) => _validate(
                emailErrorTextState,
                passwordErrorTextState,
                password: input as String,
              ),
            ),
            const SizedBox(height: Dimens.l),
          ],
        ),
      ),
    );
  }

  String? _validate(
    SameValueNotifier<String?> emailErrorTextState,
    SameValueNotifier<String?> passwordErrorTextState, {
    String? email,
    String? password,
  }) {
    String? fieldError;

    if (email != null) {
      fieldError = isEmail(email) ? null : 'Provide valid email phrase';
      emailErrorTextState.value = fieldError;
    }

    if (password != null) {
      fieldError = password.length >= _minPasswordLength
          ? null
          : 'Password must have at least $_minPasswordLength characters';
      passwordErrorTextState.value = fieldError;
    }

    final emailError = emailErrorTextState.value;
    final passwordError = passwordErrorTextState.value;

    onValidChanged?.call(emailError == null && passwordError == null);

    return fieldError;
  }
}
