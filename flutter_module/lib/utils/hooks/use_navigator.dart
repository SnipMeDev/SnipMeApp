import 'package:flutter/cupertino.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/presentation/navigation/screen_navigator.dart';
import 'package:go_router/go_router.dart';

void useNavigator(List<ScreenNavigator> navigators) {
  return use(_NavigatorHook(navigators));
}

class _NavigatorHook extends Hook<void> {
  const _NavigatorHook(this.navigators);

  final List<ScreenNavigator> navigators;

  @override
  _NavigatorState createState() => _NavigatorState(navigators);
}

class _NavigatorState extends HookState<void, _NavigatorHook> {
  _NavigatorState(this.navigators);

  final List<ScreenNavigator> navigators;
  bool isInitialized = false;

  @override
  void build(BuildContext context) {
    if (isInitialized) return;

    _setupNavigators(context);
  }

  void _setupNavigators(BuildContext context) {
    for (var navigator in navigators) {
      navigator.setRouter(GoRouter.of(context));
    }

    isInitialized = true;
  }

  @override
  Object? get debugValue => navigators;

  @override
  String get debugLabel => 'useNavigator([${navigators.join(', ')}])';
}
