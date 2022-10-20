import 'package:freezed_annotation/freezed_annotation.dart';

extension PigeonEncodeExtension on Object {
  Map<Object?, Object?> toPigeonMap() {
    try {
      return this as Map<Object?, Object?>;
    } catch (e) {
      print("Not a Pigeon object!");
      rethrow;
    }
  }
}

extension PigeonEqualsExtension on Object {
  bool equalsPigeon(Object other) {
    final thisObj = this.toPigeonMap();
    final otherObj = other.toPigeonMap();

    return const DeepCollectionEquality().equals(
      thisObj.values,
      otherObj.values,
    );
  }
}