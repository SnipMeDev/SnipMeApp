import 'package:flutter_module/presentation/providers/channel_stream_state_notifier.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../bridge/generated.g.dart';
import '../../generated/data_model.g.dart';
import 'channel_model_provider.dart';

final detailsPageStateProvider = StateNotifierProvider<
    ChannelStreamStateNotifier<DetailModelStateData, ChannelDetailsModel>,
    DetailModelStateData>(
      (ref) => ChannelStreamStateNotifier(
    initialState: DetailModelStateData(),
    model: ref.read(detailsChannelModel),
    dataStream: channelState(),
  ),
);

final detailsPageEventProvider = StateNotifierProvider<
    ChannelStreamStateNotifier<DetailModelEventData, ChannelDetailsModel>,
    DetailModelEventData>(
      (ref) => ChannelStreamStateNotifier(
    initialState: DetailModelEventData(),
    model: ref.read(detailsChannelModel),
    dataStream: channelEvent(),
  ),
);