import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class ChannelStreamStateNotifier<STATE, MODEL> extends StateNotifier<STATE> {
  ChannelStreamStateNotifier({
    required this.initialState,
    required this.model,
    required this.dataStream,
  }) : super(initialState) {
    _stateSubscription = dataStream.listen(
      (state) => switch (state) {
        STATE() => super.state = state,
        _ => print('Unknown state: $state'),
      },
    );
  }

  final STATE initialState;
  final MODEL model;
  final Stream dataStream;
  late StreamSubscription _stateSubscription;

  @override
  void dispose() {
    _stateSubscription.cancel();
    super.dispose();
  }
}
