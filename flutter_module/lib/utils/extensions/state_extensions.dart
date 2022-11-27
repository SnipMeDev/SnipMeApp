import 'package:flutter_module/model/main_model.dart';

extension MainModelStateDataExtension on MainModelStateData {
  bool equals(Object other) {
    if (other is! MainModelStateData) return false;
    return state == other.state &&
        is_loading == other.is_loading &&
        error == other.error;
  }
}

extension DetailModelStateDataExtension on DetailModelStateData {
  bool equals(Object other) {
    if (other is! DetailModelStateData) return false;
    return other.oldHash != other.newHash;
  }
}

extension DetailModelEventDataExtension on DetailModelEventData {
  bool equals(Object other) {
    if (other is! DetailModelEventData) return false;
    return other.oldHash != other.newHash;
  }
}
