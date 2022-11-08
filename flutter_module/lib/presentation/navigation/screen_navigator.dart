import 'package:go_router/go_router.dart';

abstract class ScreenNavigator {

  late GoRouter router;

  void setRouter(GoRouter router) {
    this.router = router;
  }

  void back() {
    router.pop();
  }
}
