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
    final emailErrorTextState = useSameState<String?>(null);
    final passwordErrorTextState = useSameState<String?>(null);

    final emailError = emailErrorTextState.value;
    final passwordError = passwordErrorTextState.value;

    useEffect(() {
      // TODO Correct button enabling after validation
      final isValid = emailError == null && passwordError == null;
      print("is valid = $isValid");
      onValidChanged?.call(isValid);
    }, [emailError, passwordError]);

    return SurfaceStyles.snippetCard(
      child: PaddingStyles.regular(
        Column(
          children: [
            const SizedBox(height: Dimens.l),
            TextInputField(
              label: 'Email',
              onChanged: onEmailChanged,
              validator: (input) {
                final error =
                    isEmail(input ?? '') ? null : 'Provide valid email phrase';
                emailErrorTextState.value = error;
                return error;
              },
            ),
            const SizedBox(height: Dimens.xl),
            TextInputField(
              label: 'Password',
              onChanged: onPasswordChanged,
              isPassword: true,
              validator: (input) {
                final error = input.length >= 8
                    ? null
                    : 'Password must have at least 8 characters';
                passwordErrorTextState.value = error;
                return error;
              },
            ),
            const SizedBox(height: Dimens.l),
          ],
        ),
      ),
    );
  }
}
