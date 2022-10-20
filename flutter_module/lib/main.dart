import 'package:flutter/material.dart';
import 'package:flutter_module/messages.dart';
import 'package:flutter_module/presentation/pages/main_page.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  MyApp({Key? key}) : super(key: key);

  final mainModel = MainModelApi();

  // This widgets is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'SnipMe',
      theme: ThemeData(primarySwatch: Colors.blue),
      home: MainPage(model: mainModel),
    );
  }
}
