import 'package:flutter/foundation.dart';
import 'package:flutter_module/presentation/navigation/screen_navigator.dart';
import 'package:go_router_plus/go_router_plus.dart';

class LoginNavigator extends ScreenNavigator with ChangeNotifier implements LoggedInState {
  bool _loggedIn = false;

  @override
  bool get loggedIn => _loggedIn;

  void login() {
    _loggedIn = true;
    notifyListeners();
  }

  void logout() {
    _loggedIn = false;
    notifyListeners();
  }
}
