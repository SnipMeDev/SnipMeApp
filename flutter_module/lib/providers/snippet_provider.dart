import 'package:flutter_module/model/main_model.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class MainModel extends StateNotifier<MainModelStateData?> {
  MainModel() : super(null);

  Future<void> init() async {
    state = await MainModelBridge().getState();
  }
}

final mainStateProvider =
    StateNotifierProvider<MainModel, MainModelStateData?>((ref) => MainModel());
