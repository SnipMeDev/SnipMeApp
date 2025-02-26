import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:flutter_module/generated/data_model.g.dart';

final loginChannelModel = Provider((ref) => ChannelLoginModel());
final mainChannelModel = Provider((ref) => ChannelMainModel());
final detailsChannelModel = Provider((ref) => ChannelDetailModel());