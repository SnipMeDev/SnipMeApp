import 'package:flutter_module/generated/data_model.g.dart';
import 'package:flutter_module/presentation/providers/model_bridge_provider.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_module/presentation/providers/channel_stream_state_notifier.dart';

final mainPageStateProvider = StateNotifierProvider<
    ChannelStreamStateNotifier<MainModelStateData, ChannelMainModel>,
    MainModelStateData>(
  (ref) => ChannelStreamStateNotifier(
    initialState: MainModelStateData(),
    model: ref.read(mainChannelModel),
    dataStream: channelState(),
  ),
);

final mainPageEventProvider = StateNotifierProvider<
    ChannelStreamStateNotifier<MainModelEventData, ChannelMainModel>,
    MainModelEventData>(
  (ref) => ChannelStreamStateNotifier(
    initialState: MainModelEventData(),
    model: ref.read(mainChannelModel),
    dataStream: channelEvent(),
  ),
);
