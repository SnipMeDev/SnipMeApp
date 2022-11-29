import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter_module/presentation/navigation/screen_navigator.dart';
import 'package:flutter_module/presentation/screens/details_screen.dart';
import 'package:flutter_module/utils/extensions/text_extensions.dart';
import 'package:go_router/go_router.dart';

class DetailsNavigator extends ScreenNavigator {
  String? _snippetId;

  String? get snippetId => _snippetId;

  void goToDetails(BuildContext context, String snippetId) {
    _snippetId = snippetId;
    router.push(DetailsScreen.name.route);
    print("Navigated to $_snippetId");
  }
}
