import 'package:pigeon/pigeon.dart';

// General

class Snippet {
  String? uuid;
  String? title;
  SnippetCode? code;
  SnippetLanguage? language;
  Owner? owner;
  String? modifiedAt;
  int? numberOfLikes;
  int? numberOfDislikes;
}

class SnippetCode {
  String? raw;
  List<SyntaxToken?>? tokens;
}

class SyntaxToken {
  int? start;
  int? end;
  int? color;
}

class SnippetLanguage {
  String? raw;
  SnippetLanguageType? type;
}

class Owner {
  int? id;
  String? login;
}

enum SnippetLanguageType {
  c,
  cpp,
  objective_c,
  c_sharp,
  java,
  bash,
  python,
  perl,
  ruby,
  swift,
  javascript,
  kotlin,
  coffeescript,
  rust,
  basic,
  clojure,
  css,
  dart,
  erlang,
  go,
  haskell,
  lisp,
  llvm,
  lua,
  matlab,
  ml,
  mumps,
  nemerle,
  pascal,
  r,
  rd,
  scala,
  sql,
  tex,
  vb,
  vhdl,
  tcl,
  xquery,
  yaml,
  markdown,
  json,
  xml,
  proto,
  regex,
  unknown
}

enum SnippetFilterType { all, mine, shared }

class SnippetFilter {
  SnippetFilterType? type;
}

// State

enum ModelState { loading, loaded, error }

enum MainModelEvent { none, alert, logout }

class MainModelStateData {
  ModelState? state;
  bool? is_loading;
  List<Snippet?>? data;
  String? error;
}

class MainModelEventData {
  MainModelEvent? event;
  String? message;
}

// Api

@HostApi()
abstract class MainModelApi {
  MainModelStateData getState();

  MainModelEventData getEvent();

  @TaskQueue(type: TaskQueueType.serialBackgroundThread)
  void initState();

  @TaskQueue(type: TaskQueueType.serialBackgroundThread)
  void loadNextPage();

  @TaskQueue(type: TaskQueueType.serialBackgroundThread)
  void filter(SnippetFilter filter);

  @TaskQueue(type: TaskQueueType.serialBackgroundThread)
  void logOut();

  @TaskQueue(type: TaskQueueType.serialBackgroundThread)
  void refreshSnippetUpdates();
}
