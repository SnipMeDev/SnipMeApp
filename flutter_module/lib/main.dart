import 'package:flutter/material.dart';
import 'package:flutter_module/messages.dart';
import 'package:flutter_module/presentation/pages/main_page.dart';
import 'package:flutter_module/presentation/widgets/snippet_list_item.dart';
import 'package:flutter_module/utils/mock/mock_page.dart';
import 'package:flutter_module/utils/mock/mocks.dart';

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
      home: MockPage(children: [
        SnippetListTile(snippet: Mocks.snippet),
      ]),
    );
  }
}
