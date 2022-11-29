import 'package:flutter_module/model/main_model.dart';

extension MainModelStateDataExtension on MainModelStateData {
  bool equals(Object other) {
    if (other is! MainModelStateData) return false;
    return other.oldHash != other.newHash;
  }
}

extension MainModelEventDataExtension on MainModelEventData {
  bool equals(Object other) {
    if (other is! MainModelEventData) return false;
    return other.oldHash != other.newHash;
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
