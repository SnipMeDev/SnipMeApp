import 'package:flutter_module/utils/extensions/text_extensions.dart';
import 'package:go_router_plus/go_router_plus.dart';

abstract class NamedScreen extends Screen {
  NamedScreen(this._name);

  final String _name;

  @override
  String get routeName => _name;

  @override
  String get routePath => _name.route;
}