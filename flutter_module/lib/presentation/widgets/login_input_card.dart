import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/styles/padding_styles.dart';
import 'package:flutter_module/presentation/styles/surface_styles.dart';
import 'package:flutter_module/presentation/widgets/text_input_field.dart';

class LoginInputCard extends StatelessWidget {
  const LoginInputCard({
    Key? key,
    required this.onLoginChanged,
    required this.onPasswordChanged,
  }) : super(key: key);

  final TextInputCallback onLoginChanged;
  final TextInputCallback onPasswordChanged;

  @override
  Widget build(BuildContext context) {
    return SurfaceStyles.snippetCard(
      child: PaddingStyles.regular(
        Column(
          children: [
            const SizedBox(height: Dimens.l),
            TextInputField(
              label: 'Login',
              onChanged: onLoginChanged,
            ),
            const SizedBox(height: Dimens.xl),
            TextInputField(
              label: 'Password',
              onChanged: onPasswordChanged,
              isPassword: true,
            ),
            const SizedBox(height: Dimens.l),
          ],
        ),
      ),
    );
  }
}
