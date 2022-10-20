import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/messages.dart';
import 'package:flutter_module/utils/extensions/collection_extensions.dart';
import 'package:flutter_module/utils/extensions/text_extensions.dart';

class SnippetListItem extends HookWidget {
  const SnippetListItem({
    Key? key,
    required this.snippet,
  }) : super(key: key);

  final Snippet snippet;

  @override
  Widget build(BuildContext context) {
    useEffect(() {
      print("Title = ${snippet.title}");
      print(
          "Tokens = ${snippet.code?.tokens?.map((e) => "Token = ${e?.start} : ${e?.end} -> ${e?.color}").join("\n")}");
    }, [snippet]);

    return ListTile(
      title: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Text(snippet.title ?? "Snippet"),
      ),
      subtitle: Container(
        padding: const EdgeInsets.all(8.0),
        color: Colors.white70,
        child: SelectableText.rich(
          TextSpan(
            style: const TextStyle(color: Colors.black),
            children: snippet.code?.tokens?.toSpans(
              snippet.code?.raw?.lines(5) ?? "",
              const TextStyle(color: Colors.black),
            ),
          ),
          minLines: 1,
          maxLines: 5,
          onTap: () => print('To copy text go to details'),
          toolbarOptions: ToolbarOptions(
            copy: true,
            selectAll: true,
          ),
          showCursor: true,
          cursorWidth: 2,
          cursorColor: Colors.red,
          cursorRadius: Radius.circular(5),
        ),
      ),
    );
  }
}
