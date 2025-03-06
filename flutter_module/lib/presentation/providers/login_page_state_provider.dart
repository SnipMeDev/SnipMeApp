import 'package:flutter_module/generated/data_model.g.dart';
import 'package:flutter_module/presentation/providers/channel_stream_state_notifier.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import 'channel_model_provider.dart';

final loginPageStateProvider = StateNotifierProvider<
    ChannelStreamStateNotifier<LoginModelStateData, ChannelLoginModel>,
    LoginModelStateData>(
  (ref) => ChannelStreamStateNotifier(
    initialState: LoginModelStateData(),
    model: ref.read(loginChannelModel),
    dataStream: channelState(),
  ),
);

final loginPageEventProvider = StateNotifierProvider<
    ChannelStreamStateNotifier<LoginModelEventData, ChannelLoginModel>,
    LoginModelEventData>(
  (ref) => ChannelStreamStateNotifier(
    initialState: LoginModelEventData(),
    model: ref.read(loginChannelModel),
    dataStream: channelEvent(),
  ),
);
