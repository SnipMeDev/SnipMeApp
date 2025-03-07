import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(
  PigeonOptions(
    dartOut: 'lib/generated/data_model.g.dart',
    // Generate in parent android module
    kotlinOut:
        '../app/src/main/java/dev/snipme/snipmeapp/channel/DataModel.g.kt',
    kotlinOptions: KotlinOptions(package: 'dev.snipme.snipmeapp.channel'),
  ),
)

class Snippet {
  String? uuid;
  String? title;
  SnippetCode? code;
  SnippetLanguage? language;
  Owner? owner;
  bool? isOwner;
  String? timeAgo;
  int? voteResult;
  bool? isPrivate;
  bool? isFavorite;
  bool? isSaved;
  bool? isToDelete;
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
  objectiveC,
  cSharp,
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

enum ModelState { loading, loaded, error }

enum MainModelEvent { none, alert, logout }

enum DetailsModelEvent { none, alert, deleted }

enum LoginModelEvent { none, logged }

sealed class ModelStateData {}

sealed class ModelEventData {}

class MainModelStateData extends ModelStateData {
  ModelState? state;
  bool? isLoading;
  List<Snippet?>? data;
  SnippetFilter? filter;
  String? error;
}

class MainModelEventData extends ModelEventData {
  MainModelEvent? event;
  String? message;
}

class DetailsModelStateData extends ModelStateData {
  ModelState? state;
  bool? isLoading;
  Snippet? data;
  String? error;
}

class DetailsModelEventData extends ModelEventData {
  DetailsModelEvent? event;
  String? value;
}

class LoginModelStateData extends ModelStateData {
  ModelState? state;
  bool? isLoading;
}

class LoginModelEventData extends ModelEventData {
  LoginModelEvent? event;
}

@EventChannelApi()
abstract class ChannelModelEventApi {
  ModelStateData channelState();

  ModelEventData channelEvent();
}

@HostApi()
abstract class ChannelMainModel {
  void resetEvent();

  @TaskQueue(type: TaskQueueType.serialBackgroundThread)
  void initState();

  @TaskQueue(type: TaskQueueType.serialBackgroundThread)
  void filterLanguage(String language, bool isSelected);

  @TaskQueue(type: TaskQueueType.serialBackgroundThread)
  void filterScope(String scope);

  @TaskQueue(type: TaskQueueType.serialBackgroundThread)
  void logOut();
}

@HostApi()
abstract class ChannelDetailsModel {
  void resetEvent();

  void load(String uuid);

  void toggleFavorite();

  void saveImage(Uint8List image);

  void copyToClipboard();

  void shareImage(Uint8List image);

  void delete();
}

@HostApi()
abstract class ChannelLoginModel {
  void loginOrRegister(String email, String password);

  void checkLoginState();

  void resetEvent();
}
