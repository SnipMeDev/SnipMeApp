import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/utils/hooks/use_same_state.dart';

typedef StateEqualsListener = bool Function(Object, Object);

ValueNotifier<T> useObservableState<T>(
  T initialData,
  Future<T> Function() getSource,
  StateEqualsListener stateEquals, [
  List<Object> cancelKeys = const [],
  List<Object> refreshKeys = const [],
]) {
  final state = useSameState(initialData);

  final timer = useMemoized(
    () => Timer.periodic(const Duration(milliseconds: 500), (_) async {
      final T newData = await getSource();
      if (!stateEquals(state.value as Object, newData as Object)) {
        state.value = newData;
      }
    }),
    refreshKeys,
  );

  useEffect(() {
    return () => timer.cancel();
  }, cancelKeys);

  return state;
}
