import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

extension NavigatorPopExtension on BuildContext {
  void popOrExit() {
    if (Navigator.canPop(this)) {
      Navigator.pop(this);
    } else {
      SystemNavigator.pop();
    }
  }
}
