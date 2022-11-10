import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter_module/model/main_model.dart';
import 'package:flutter_module/presentation/navigation/screen_navigator.dart';
import 'package:flutter_module/presentation/screens/details_screen.dart';
import 'package:flutter_module/utils/extensions/text_extensions.dart';
import 'package:go_router/go_router.dart';

class DetailsNavigator extends ScreenNavigator {
  Snippet? _snippet;

  Snippet? get snippet => _snippet;

  void goToDetails(BuildContext context, Snippet snippet) {
    _snippet = snippet;
    router.push(DetailsScreen.name.route);
  }
}
