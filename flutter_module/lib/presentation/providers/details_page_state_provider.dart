import 'package:flutter_module/presentation/providers/channel_stream_state_notifier.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../generated/data_model.g.dart';
import 'channel_model_provider.dart';

final detailsPageStateProvider = StateNotifierProvider<
    ChannelStreamStateNotifier<DetailsModelStateData, ChannelDetailsModel>,
    DetailsModelStateData>(
  (ref) => ChannelStreamStateNotifier(
    initialState: DetailsModelStateData(),
    model: ref.read(detailsChannelModel),
    dataStream: channelState(),
  ),
);

final detailsPageEventProvider = StateNotifierProvider<
    ChannelStreamStateNotifier<DetailsModelEventData, ChannelDetailsModel>,
    DetailsModelEventData>(
  (ref) => ChannelStreamStateNotifier(
    initialState: DetailsModelEventData(),
    model: ref.read(detailsChannelModel),
    dataStream: channelEvent(),
  ),
);
