import 'package:flutter/widgets.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';

class PaddingStyles extends Padding {

  const PaddingStyles.regular(Widget child, {Key? key})
      : super(key: key, padding: const EdgeInsets.all(Dimens.l));
}
