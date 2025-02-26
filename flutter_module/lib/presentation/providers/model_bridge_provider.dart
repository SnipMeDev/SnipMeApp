import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:flutter_module/generated/data_model.g.dart';

final loginBridge = Provider((ref) => LoginModelBridge());
final mainBridge = Provider((ref) => MainModelBridge());
final detailsBridge = Provider((ref) => DetailModelBridge());