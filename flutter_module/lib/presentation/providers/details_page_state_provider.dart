import 'package:flutter_module/generated/data_model.g.dart';
import 'package:flutter_module/presentation/providers/model_bridge_provider.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

final detailsPageStateProvider =
    StateNotifierProvider<_DetailPageStateNotifier, DetailModelStateData>(
  (ref) => _DetailPageStateNotifier(model: ref.read(detailsChannelModel)),
);

final detailsPageEventProvider = StateNotifierProvider<_DetailPageEventNotifier, DetailModelEventData>(
  (ref) => _DetailPageEventNotifier(model: ref.read(detailsChannelModel)),
);

class _DetailPageStateNotifier extends StateNotifier<DetailModelStateData> {
  _DetailPageStateNotifier({
    required this.model,
  }) : super(DetailModelStateData()) {
    // Simulate hook's state & event refreshing
    Stream.periodic(
      const Duration(milliseconds: 500),
      (_) => model.getState(),
    ).listen((newState) async => state = await newState);
  }

  final ChannelDetailModel model;
}

class _DetailPageEventNotifier extends StateNotifier<DetailModelEventData> {
  _DetailPageEventNotifier({
    required this.model,
  }) : super(DetailModelEventData()) {
    // Simulate hook's state & event refreshing
    Stream.periodic(
      const Duration(milliseconds: 500),
      (_) => model.getEvent(),
    ).listen((newEvent) async => state = await newEvent);
  }

  final ChannelDetailModel model;
}
