import 'package:flutter_module/generated/data_model.g.dart';
import 'package:flutter_module/presentation/providers/model_bridge_provider.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

final loginPageStateProvider =
    StateNotifierProvider<_LoginPageStateNotifier, LoginModelStateData>(
  (ref) => _LoginPageStateNotifier(loginBridge: ref.read(loginBridge)),
);

final loginPageEventProvider = StateNotifierProvider<_LoginPageEventNotifier, LoginModelEventData>(
  (ref) => _LoginPageEventNotifier(loginBridge: ref.read(loginBridge)),
);

class _LoginPageStateNotifier extends StateNotifier<LoginModelStateData> {
  _LoginPageStateNotifier({
    required this.loginBridge,
  }) : super(LoginModelStateData()) {
    // Simulate hook's state & event refreshing
    Stream.periodic(
      const Duration(milliseconds: 500),
      (_) => loginBridge.getState(),
    ).listen((newState) async => state = await newState);
  }

  final LoginModelBridge loginBridge;
}

class _LoginPageEventNotifier extends StateNotifier<LoginModelEventData> {
  _LoginPageEventNotifier({
    required this.loginBridge,
  }) : super(LoginModelEventData()) {
    // Simulate hook's state & event refreshing
    Stream.periodic(
      const Duration(milliseconds: 500),
      (_) => loginBridge.getEvent(),
    ).listen((newEvent) async => state = await newEvent);
  }

  final LoginModelBridge loginBridge;
}
