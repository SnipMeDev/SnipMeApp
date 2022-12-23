import 'package:pigeon/pigeon.dart';

// General

class Snippet {
  String? uuid;
  String? title;
  SnippetCode? code;
  SnippetLanguage? language;
  Owner? owner;
  bool? isOwner;
  String? timeAgo;
  int? voteResult;
  UserReaction? userReaction;
  bool? isPrivate;
  bool? isLiked;
  bool? isDisliked;
  bool? isSaved;
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
  List<String?>? languages;
  List<String?>? selectedLanguages;
  List<String?>? scopes;
  String? selectedScope;
}

enum UserReaction {
  none,
  like,
  dislike
}

// State

enum ModelState { loading, loaded, error }

enum MainModelEvent { none, alert, logout }

enum DetailModelEvent { none, saved, deleted }

enum LoginModelEvent { none, logged }

class MainModelStateData {
  ModelState? state;
  bool? is_loading;
  List<Snippet?>? data;
  SnippetFilter? filter;
  String? error;
  int? oldHash;
  int? newHash;
}

class MainModelEventData {
  MainModelEvent? event;
  String? message;
  int? oldHash;
  int? newHash;
}

class DetailModelStateData {
  ModelState? state;
  bool? is_loading;
  Snippet? data;
  String? error;
  int? oldHash;
  int? newHash;
}

class DetailModelEventData {
  DetailModelEvent? event;
  String? value;
  int? oldHash;
  int? newHash;
}

class LoginModelStateData {
  ModelState? state;
  bool? is_loading;
  int? oldHash;
  int? newHash;
}

class LoginModelEventData {
  LoginModelEvent? event;
  int? oldHash;
  int? newHash;
}

// Api

@HostApi()
abstract class MainModelBridge {
  MainModelStateData getState();

  MainModelEventData getEvent();

  void resetEvent();

  @TaskQueue(type: TaskQueueType.serialBackgroundThread)
  void initState();

  @TaskQueue(type: TaskQueueType.serialBackgroundThread)
  void filterLanguage(String language, bool isSelected);

  @TaskQueue(type: TaskQueueType.serialBackgroundThread)
  void filterScope(String scope);

  @TaskQueue(type: TaskQueueType.serialBackgroundThread)
  void logOut();

  @TaskQueue(type: TaskQueueType.serialBackgroundThread)
  void refreshSnippetUpdates();
}

@HostApi()
abstract class DetailModelBridge {
  DetailModelStateData getState();

  DetailModelEventData getEvent();

  void resetEvent();

  void load(String uuid);

  void like();

  void dislike();

  void save();

  void copyToClipboard();

  void share();

  void delete();
}

@HostApi()
abstract class LoginModelBridge {
  LoginModelStateData getState();

  LoginModelEventData getEvent();

  void loginOrRegister(String email, String password);

  void checkLoginState();

  void resetEvent();
}