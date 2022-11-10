import 'package:flutter/material.dart';
import 'package:flutter_module/model/main_model.dart';

class Mocks {
  static final snippet = Snippet(
    uuid: '',
    title: 'New snippet',
    owner: Owner(id: 0, login: 'Snippet owner'),
    timeAgo: '2 days ago',
    voteResult: 32,
    isLiked: true,
    isDisliked: false,
    language: SnippetLanguage(
      raw: 'Kotlin',
      type: SnippetLanguageType.kotlin,
    ),
    code: SnippetCode(
      raw: 'class NewClass { }',
      tokens: [
        SyntaxToken(
          start: 0,
          end: 5,
          color: Colors.amber.value,
        )
      ],
    ),
  );
}
