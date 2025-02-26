import 'package:flutter_module/generated/data_model.g.dart';
import 'package:flutter_module/presentation/providers/model_bridge_provider.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

final mainPageStateProvider =
    StateNotifierProvider<_MainPageStateNotifier, MainModelStateData>(
  (ref) => _MainPageStateNotifier(mainBridge: ref.read(mainBridge)),
);

final mainPageEventProvider = StateNotifierProvider<_MainPageEventNotifier, MainModelEventData>(
  (ref) => _MainPageEventNotifier(mainBridge: ref.read(mainBridge)),
);

class _MainPageStateNotifier extends StateNotifier<MainModelStateData> {
  _MainPageStateNotifier({
    required this.mainBridge,
  }) : super(MainModelStateData()) {
    // Simulate hook's state & event refreshing
    Stream.periodic(
      const Duration(milliseconds: 500),
      (_) => mainBridge.getState(),
    ).listen((newState) async => state = await newState);
  }

  final MainModelBridge mainBridge;
}

class _MainPageEventNotifier extends StateNotifier<MainModelEventData> {
  _MainPageEventNotifier({
    required this.mainBridge,
  }) : super(MainModelEventData()) {
    // Simulate hook's state & event refreshing
    Stream.periodic(
      const Duration(milliseconds: 500),
      (_) => mainBridge.getEvent(),
    ).listen((newEvent) async => state = await newEvent);
  }

  final MainModelBridge mainBridge;
}
