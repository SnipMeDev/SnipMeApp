import 'package:flutter/foundation.dart';

class DetailsNavigator with ChangeNotifier {
  String? _snippetId;

  String? get snippetId => _snippetId;

  void goToDetails(String snippetId) {
    _snippetId = snippetId;
    notifyListeners();
  }
}