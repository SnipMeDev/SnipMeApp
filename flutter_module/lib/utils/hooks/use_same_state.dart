import 'package:flutter/widgets.dart';
import 'package:flutter_hooks/flutter_hooks.dart';

class SameValueNotifier<T> extends ValueNotifier<T> {
  SameValueNotifier(this._value) : super(_value);

  @override
  T get value => _value;
  T _value;
  @override
  set value(T newValue) {
    _value = newValue;
    notifyListeners();
  }
}

SameValueNotifier<T> useSameState<T>(T initialData) {
  return use(_SameStateHook(initialData: initialData));
}

class _SameStateHook<T> extends Hook<SameValueNotifier<T>> {
  const _SameStateHook({required this.initialData});

  final T initialData;

  @override
  _SameStateHookState<T> createState() => _SameStateHookState();
}

class _SameStateHookState<T> extends HookState<SameValueNotifier<T>, _SameStateHook<T>> {
  late final _state = SameValueNotifier<T>(hook.initialData)
    ..addListener(_listener);

  @override
  void dispose() {
    _state.dispose();
  }

  @override
  SameValueNotifier<T> build(BuildContext context) => _state;

  void _listener() {
    setState(() {});
  }

  @override
  Object? get debugValue => _state.value;

  @override
  String get debugLabel => 'useState<$T>';
}