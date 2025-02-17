import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:flutter_module/model/main_model.dart';

final loginBridge = Provider((ref) => LoginModelBridge());
final mainBridge = Provider((ref) => MainModelBridge());
final detailBridge = Provider((ref) => DetailModelBridge());