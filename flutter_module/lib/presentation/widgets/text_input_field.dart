import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/presentation/styles/color_styles.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';

typedef TextInputCallback = Function(String value);

class TextInputField extends HookWidget {
  const TextInputField({
    Key? key,
    required this.label,
    this.isPassword = false,
    this.onChanged,
  }) : super(key: key);

  final String label;
  final bool isPassword;
  final TextInputCallback? onChanged;

  @override
  Widget build(BuildContext context) {
    final controller = useTextEditingController();
    final value = useValueListenable(controller);
    final shouldShow = useState(false);
    final passwordVisible = shouldShow.value;

    useEffect(() {
      onChanged?.call(value.text);
      return null;
    }, [value.text]);

    return TextField(
      obscureText: isPassword && !shouldShow.value,
      controller: controller,
      cursorColor: ColorStyles.accent(),
      decoration: InputDecoration(
        labelText: label,
        floatingLabelStyle: TextStyle(color: ColorStyles.accent()),
        border: OutlineInputBorder(
          borderSide: BorderSide(
            color: ColorStyles.pageBackground(),
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
