import 'package:flutter_module/model/main_model.dart';
import 'package:flutter_module/presentation/providers/model_bridge_provider.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

final detailPageStateProvider =
    StateNotifierProvider<_DetailPageStateNotifier, DetailModelStateData>(
  (ref) => _DetailPageStateNotifier(detailBridge: ref.read(detailBridge)),
);

final detailPageEventProvider = StateNotifierProvider<_DetailPageEventNotifier, DetailModelEventData>(
  (ref) => _DetailPageEventNotifier(detailBridge: ref.read(detailBridge)),
);

class _DetailPageStateNotifier extends StateNotifier<DetailModelStateData> {
  _DetailPageStateNotifier({
    required this.detailBridge,
  }) : super(DetailModelStateData()) {
    // Simulate hook's state & event refreshing
    Stream.periodic(
      const Duration(milliseconds: 500),
      (_) => detailBridge.getState(),
    ).listen((newState) async => state = await newState);
  }

  final DetailModelBridge detailBridge;
}

class _DetailPageEventNotifier extends StateNotifier<DetailModelEventData> {
  _DetailPageEventNotifier({
    required this.detailBridge,
  }) : super(DetailModelEventData()) {
    // Simulate hook's state & event refreshing
    Stream.periodic(
      const Duration(milliseconds: 500),
      (_) => detailBridge.getEvent(),
    ).listen((newEvent) async => state = await newEvent);
  }

  final DetailModelBridge detailBridge;
}
