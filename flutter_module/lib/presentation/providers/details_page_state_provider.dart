import 'package:flutter_module/model/main_model.dart';
import 'package:flutter_module/presentation/providers/model_bridge_provider.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

final detailsPageStateProvider =
    StateNotifierProvider<_DetailPageStateNotifier, DetailModelStateData>(
  (ref) => _DetailPageStateNotifier(detailsBridge: ref.read(detailsBridge)),
);

final detailsPageEventProvider = StateNotifierProvider<_DetailPageEventNotifier, DetailModelEventData>(
  (ref) => _DetailPageEventNotifier(detailsBridge: ref.read(detailsBridge)),
);

class _DetailPageStateNotifier extends StateNotifier<DetailModelStateData> {
  _DetailPageStateNotifier({
    required this.detailsBridge,
  }) : super(DetailModelStateData()) {
    // Simulate hook's state & event refreshing
    Stream.periodic(
      const Duration(milliseconds: 500),
      (_) => detailsBridge.getState(),
    ).listen((newState) async => state = await newState);
  }

  final DetailModelBridge detailsBridge;
}

class _DetailPageEventNotifier extends StateNotifier<DetailModelEventData> {
  _DetailPageEventNotifier({
    required this.detailsBridge,
  }) : super(DetailModelEventData()) {
    // Simulate hook's state & event refreshing
    Stream.periodic(
      const Duration(milliseconds: 500),
      (_) => detailsBridge.getEvent(),
    ).listen((newEvent) async => state = await newEvent);
  }

  final DetailModelBridge detailsBridge;
}
