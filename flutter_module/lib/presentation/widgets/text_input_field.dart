import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/presentation/styles/color_styles.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/utils/hooks/use_same_state.dart';

typedef TextInputCallback = Function(String value);

class TextInputField extends HookWidget {
  TextInputField({
    Key? key,
    required this.label,
    this.isPassword = false,
    this.onChanged,
    this.validator,
  }) : super(key: key);

  final String label;
  final bool isPassword;
  final TextInputCallback? onChanged;
  final FormFieldValidator? validator;

  @override
  Widget build(BuildContext context) {
    final controller = useTextEditingController();
    final shouldShow = useState(false);
    final error = useState<String?>(null);
    final passwordVisible = shouldShow.value;

    useEffect(() {
      controller.addListener(() {
        onChanged?.call(controller.text);
      });
    }, []);

    useEffect(() {
      error.value =
          controller.text.isNotEmpty ? validator?.call(controller.text) : null;
    }, [controller.text]);

    return TextFormField(
      autovalidateMode: AutovalidateMode.onUserInteraction,
      obscureText: isPassword && !shouldShow.value,
      controller: controller,
      cursorColor: ColorStyles.accent(),
      decoration: InputDecoration(
        errorText: error.value,
        focusedErrorBorder: const OutlineInputBorder(
          borderSide: BorderSide(
            color: Colors.red,
            width: Dimens.inputBorderWidth,
          ),
        ),
        errorBorder: const OutlineInputBorder(
          borderSide: BorderSide(
            color: Colors.red,
            width: Dimens.inputBorderWidth,
          ),
        ),
        labelText: label,
        floatingLabelStyle: TextStyle(
          color: error.value == null ? ColorStyles.accent() : Colors.red,
        ),
        enabledBorder: const OutlineInputBorder(
          borderSide: BorderSide(
            color: Colors.grey,
            width: Dimens.inputBorderWidth,
          ),
        ),
        focusedBorder: OutlineInputBorder(
          borderSide: BorderSide(
            color: ColorStyles.accent(),
            width: Dimens.inputBorderWidth,
          ),
        ),
        suffixIcon: isPassword
            ? InkWell(
                radius: Dimens.xl,
                onTap: () => shouldShow.value = !passwordVisible,
                child: passwordVisible
                    ? const Icon(Icons.visibility_off, color: Colors.black)
                    : const Icon(Icons.visibility, color: Colors.black),
              )
            : null,
      ),
    );
  }
}
