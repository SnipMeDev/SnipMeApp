import 'dart:convert';

import 'package:time_elapsed/time_elapsed.dart';

extension TextExtensions on String {
  List<String> splitByIndices(List<int> splitters) {
    List<String> result = [];

    if (splitters.any((splitIndex) => splitIndex < 0)) {
      throw Exception("Index for word must be at least 0");
    }

    if (splitters.any((splitIndex) => splitIndex < 0)) {
      throw Exception("Index for word must at most ${length}");
    }

    if (splitters.isEmpty) {
      throw Exception("There must be at least one splitter provided");
    }

    splitters.sort((a, b) => a.compareTo(b));

    if (splitters.first > 0) {
      splitters.insert(0, 0);
    }

    if (splitters.last < length) {
      splitters.add(length);
    }

    for (int i = 0; i < splitters.length; i++) {
      final splitter = splitters[i];

      if (splitter == splitters.last) {
        break;
      }

      final nextSplitter = splitters[i + 1];
      result.add(substring(splitter, nextSplitter));
    }

    return result;
  }

  String lines(int count) {
    final split = const LineSplitter().convert(this);

    if (split.length == 1) {
      return split.first;
    }

    return split.join('\n');
  }

  String toElapsedTime() {
    final customDate = CustomTimeElapsed(
      minutes: 'minutes',
      hours: 'hours',
      days: 'days',
      now: 'now',
      seconds: 'seconds',
      weeks: 'weeks',
    );

    return TimeElapsed.fromDateStr(this).toCustomTimeElapsed(customDate);
  }
}