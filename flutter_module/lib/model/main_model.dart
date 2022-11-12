// Autogenerated from Pigeon (v4.2.3), do not edit directly.
// See also: https://pub.dev/packages/pigeon
// ignore_for_file: public_member_api_docs, non_constant_identifier_names, avoid_as, unused_import, unnecessary_parenthesis, prefer_null_aware_operators, omit_local_variable_types, unused_shown_name, unnecessary_import
import 'dart:async';
import 'dart:typed_data' show Float64List, Int32List, Int64List, Uint8List;

import 'package:flutter/foundation.dart' show ReadBuffer, WriteBuffer;
import 'package:flutter/services.dart';

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
  unknown,
}

enum SnippetFilterType {
  all,
  mine,
  shared,
}

enum UserReaction {
  none,
  like,
  dislike,
}

enum ModelState {
  loading,
  loaded,
  error,
}

enum MainModelEvent {
  none,
  alert,
  logout,
}

class Snippet {
  Snippet({
    this.uuid,
    this.title,
    this.code,
    this.language,
    this.owner,
    this.isOwner,
    this.timeAgo,
    this.voteResult,
    this.userReaction,
    this.isPrivate,
    this.isLiked,
    this.isDisliked,
    this.isSaved,
  });

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

  Object encode() {
    final Map<Object?, Object?> pigeonMap = <Object?, Object?>{};
    pigeonMap['uuid'] = uuid;
    pigeonMap['title'] = title;
    pigeonMap['code'] = code?.encode();
    pigeonMap['language'] = language?.encode();
    pigeonMap['owner'] = owner?.encode();
    pigeonMap['isOwner'] = isOwner;
    pigeonMap['timeAgo'] = timeAgo;
    pigeonMap['voteResult'] = voteResult;
    pigeonMap['userReaction'] = userReaction?.index;
    pigeonMap['isPrivate'] = isPrivate;
    pigeonMap['isLiked'] = isLiked;
    pigeonMap['isDisliked'] = isDisliked;
    pigeonMap['isSaved'] = isSaved;
    return pigeonMap;
  }

  static Snippet decode(Object message) {
    final Map<Object?, Object?> pigeonMap = message as Map<Object?, Object?>;
    return Snippet(
      uuid: pigeonMap['uuid'] as String?,
      title: pigeonMap['title'] as String?,
      code: pigeonMap['code'] != null
          ? SnippetCode.decode(pigeonMap['code']!)
          : null,
      language: pigeonMap['language'] != null
          ? SnippetLanguage.decode(pigeonMap['language']!)
          : null,
      owner: pigeonMap['owner'] != null
          ? Owner.decode(pigeonMap['owner']!)
          : null,
      isOwner: pigeonMap['isOwner'] as bool?,
      timeAgo: pigeonMap['timeAgo'] as String?,
      voteResult: pigeonMap['voteResult'] as int?,
      userReaction: pigeonMap['userReaction'] != null
          ? UserReaction.values[pigeonMap['userReaction']! as int]
          : null,
      isPrivate: pigeonMap['isPrivate'] as bool?,
      isLiked: pigeonMap['isLiked'] as bool?,
      isDisliked: pigeonMap['isDisliked'] as bool?,
      isSaved: pigeonMap['isSaved'] as bool?,
    );
  }
}

class SnippetCode {
  SnippetCode({
    this.raw,
    this.tokens,
  });

  String? raw;
  List<SyntaxToken?>? tokens;

  Object encode() {
    final Map<Object?, Object?> pigeonMap = <Object?, Object?>{};
    pigeonMap['raw'] = raw;
    pigeonMap['tokens'] = tokens;
    return pigeonMap;
  }

  static SnippetCode decode(Object message) {
    final Map<Object?, Object?> pigeonMap = message as Map<Object?, Object?>;
    return SnippetCode(
      raw: pigeonMap['raw'] as String?,
      tokens: (pigeonMap['tokens'] as List<Object?>?)?.cast<SyntaxToken?>(),
    );
  }
}

class SyntaxToken {
  SyntaxToken({
    this.start,
    this.end,
    this.color,
  });

  int? start;
  int? end;
  int? color;

  Object encode() {
    final Map<Object?, Object?> pigeonMap = <Object?, Object?>{};
    pigeonMap['start'] = start;
    pigeonMap['end'] = end;
    pigeonMap['color'] = color;
    return pigeonMap;
  }

  static SyntaxToken decode(Object message) {
    final Map<Object?, Object?> pigeonMap = message as Map<Object?, Object?>;
    return SyntaxToken(
      start: pigeonMap['start'] as int?,
      end: pigeonMap['end'] as int?,
      color: pigeonMap['color'] as int?,
    );
  }
}

class SnippetLanguage {
  SnippetLanguage({
    this.raw,
    this.type,
  });

  String? raw;
  SnippetLanguageType? type;

  Object encode() {
    final Map<Object?, Object?> pigeonMap = <Object?, Object?>{};
    pigeonMap['raw'] = raw;
    pigeonMap['type'] = type?.index;
    return pigeonMap;
  }

  static SnippetLanguage decode(Object message) {
    final Map<Object?, Object?> pigeonMap = message as Map<Object?, Object?>;
    return SnippetLanguage(
      raw: pigeonMap['raw'] as String?,
      type: pigeonMap['type'] != null
          ? SnippetLanguageType.values[pigeonMap['type']! as int]
          : null,
    );
  }
}

class Owner {
  Owner({
    this.id,
    this.login,
  });

  int? id;
  String? login;

  Object encode() {
    final Map<Object?, Object?> pigeonMap = <Object?, Object?>{};
    pigeonMap['id'] = id;
    pigeonMap['login'] = login;
    return pigeonMap;
  }

  static Owner decode(Object message) {
    final Map<Object?, Object?> pigeonMap = message as Map<Object?, Object?>;
    return Owner(
      id: pigeonMap['id'] as int?,
      login: pigeonMap['login'] as String?,
    );
  }
}

class SnippetFilter {
  SnippetFilter({
    this.type,
  });

  SnippetFilterType? type;

  Object encode() {
    final Map<Object?, Object?> pigeonMap = <Object?, Object?>{};
    pigeonMap['type'] = type?.index;
    return pigeonMap;
  }

  static SnippetFilter decode(Object message) {
    final Map<Object?, Object?> pigeonMap = message as Map<Object?, Object?>;
    return SnippetFilter(
      type: pigeonMap['type'] != null
          ? SnippetFilterType.values[pigeonMap['type']! as int]
          : null,
    );
  }
}

class MainModelStateData {
  MainModelStateData({
    this.state,
    this.is_loading,
    this.data,
    this.error,
  });

  ModelState? state;
  bool? is_loading;
  List<Snippet?>? data;
  String? error;

  Object encode() {
    final Map<Object?, Object?> pigeonMap = <Object?, Object?>{};
    pigeonMap['state'] = state?.index;
    pigeonMap['is_loading'] = is_loading;
    pigeonMap['data'] = data;
    pigeonMap['error'] = error;
    return pigeonMap;
  }

  static MainModelStateData decode(Object message) {
    final Map<Object?, Object?> pigeonMap = message as Map<Object?, Object?>;
    return MainModelStateData(
      state: pigeonMap['state'] != null
          ? ModelState.values[pigeonMap['state']! as int]
          : null,
      is_loading: pigeonMap['is_loading'] as bool?,
      data: (pigeonMap['data'] as List<Object?>?)?.cast<Snippet?>(),
      error: pigeonMap['error'] as String?,
    );
  }
}

class MainModelEventData {
  MainModelEventData({
    this.event,
    this.message,
  });

  MainModelEvent? event;
  String? message;

  Object encode() {
    final Map<Object?, Object?> pigeonMap = <Object?, Object?>{};
    pigeonMap['event'] = event?.index;
    pigeonMap['message'] = message;
    return pigeonMap;
  }

  static MainModelEventData decode(Object message) {
    final Map<Object?, Object?> pigeonMap = message as Map<Object?, Object?>;
    return MainModelEventData(
      event: pigeonMap['event'] != null
          ? MainModelEvent.values[pigeonMap['event']! as int]
          : null,
      message: pigeonMap['message'] as String?,
    );
  }
}

class DetailModelStateData {
  DetailModelStateData({
    this.state,
    this.is_loading,
    this.data,
    this.error,
  });

  ModelState? state;
  bool? is_loading;
  Snippet? data;
  String? error;

  Object encode() {
    final Map<Object?, Object?> pigeonMap = <Object?, Object?>{};
    pigeonMap['state'] = state?.index;
    pigeonMap['is_loading'] = is_loading;
    pigeonMap['data'] = data?.encode();
    pigeonMap['error'] = error;
    return pigeonMap;
  }

  static DetailModelStateData decode(Object message) {
    final Map<Object?, Object?> pigeonMap = message as Map<Object?, Object?>;
    return DetailModelStateData(
      state: pigeonMap['state'] != null
          ? ModelState.values[pigeonMap['state']! as int]
          : null,
      is_loading: pigeonMap['is_loading'] as bool?,
      data: pigeonMap['data'] != null
          ? Snippet.decode(pigeonMap['data']!)
          : null,
      error: pigeonMap['error'] as String?,
    );
  }
}

class _MainModelBridgeCodec extends StandardMessageCodec{
  const _MainModelBridgeCodec();
  @override
  void writeValue(WriteBuffer buffer, Object? value) {
    if (value is MainModelEventData) {
      buffer.putUint8(128);
      writeValue(buffer, value.encode());
    } else 
    if (value is MainModelStateData) {
      buffer.putUint8(129);
      writeValue(buffer, value.encode());
    } else 
    if (value is Owner) {
      buffer.putUint8(130);
      writeValue(buffer, value.encode());
    } else 
    if (value is Snippet) {
      buffer.putUint8(131);
      writeValue(buffer, value.encode());
    } else 
    if (value is SnippetCode) {
      buffer.putUint8(132);
      writeValue(buffer, value.encode());
    } else 
    if (value is SnippetFilter) {
      buffer.putUint8(133);
      writeValue(buffer, value.encode());
    } else 
    if (value is SnippetLanguage) {
      buffer.putUint8(134);
      writeValue(buffer, value.encode());
    } else 
    if (value is SyntaxToken) {
      buffer.putUint8(135);
      writeValue(buffer, value.encode());
    } else 
{
      super.writeValue(buffer, value);
    }
  }
  @override
  Object? readValueOfType(int type, ReadBuffer buffer) {
    switch (type) {
      case 128:       
        return MainModelEventData.decode(readValue(buffer)!);
      
      case 129:       
        return MainModelStateData.decode(readValue(buffer)!);
      
      case 130:       
        return Owner.decode(readValue(buffer)!);
      
      case 131:       
        return Snippet.decode(readValue(buffer)!);
      
      case 132:       
        return SnippetCode.decode(readValue(buffer)!);
      
      case 133:       
        return SnippetFilter.decode(readValue(buffer)!);
      
      case 134:       
        return SnippetLanguage.decode(readValue(buffer)!);
      
      case 135:       
        return SyntaxToken.decode(readValue(buffer)!);
      
      default:      
        return super.readValueOfType(type, buffer);
      
    }
  }
}

class MainModelBridge {
  /// Constructor for [MainModelBridge].  The [binaryMessenger] named argument is
  /// available for dependency injection.  If it is left null, the default
  /// BinaryMessenger will be used which routes to the host platform.
  MainModelBridge({BinaryMessenger? binaryMessenger}) : _binaryMessenger = binaryMessenger;
  final BinaryMessenger? _binaryMessenger;

  static const MessageCodec<Object?> codec = _MainModelBridgeCodec();

  Future<MainModelStateData> getState() async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.MainModelBridge.getState', codec, binaryMessenger: _binaryMessenger);
    final Map<Object?, Object?>? replyMap =
        await channel.send(null) as Map<Object?, Object?>?;
    if (replyMap == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyMap['error'] != null) {
      final Map<Object?, Object?> error = (replyMap['error'] as Map<Object?, Object?>?)!;
      throw PlatformException(
        code: (error['code'] as String?)!,
        message: error['message'] as String?,
        details: error['details'],
      );
    } else if (replyMap['result'] == null) {
      throw PlatformException(
        code: 'null-error',
        message: 'Host platform returned null value for non-null return value.',
      );
    } else {
      return (replyMap['result'] as MainModelStateData?)!;
    }
  }

  Future<MainModelEventData> getEvent() async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.MainModelBridge.getEvent', codec, binaryMessenger: _binaryMessenger);
    final Map<Object?, Object?>? replyMap =
        await channel.send(null) as Map<Object?, Object?>?;
    if (replyMap == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyMap['error'] != null) {
      final Map<Object?, Object?> error = (replyMap['error'] as Map<Object?, Object?>?)!;
      throw PlatformException(
        code: (error['code'] as String?)!,
        message: error['message'] as String?,
        details: error['details'],
      );
    } else if (replyMap['result'] == null) {
      throw PlatformException(
        code: 'null-error',
        message: 'Host platform returned null value for non-null return value.',
      );
    } else {
      return (replyMap['result'] as MainModelEventData?)!;
    }
  }

  Future<void> initState() async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.MainModelBridge.initState', codec, binaryMessenger: _binaryMessenger);
    final Map<Object?, Object?>? replyMap =
        await channel.send(null) as Map<Object?, Object?>?;
    if (replyMap == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyMap['error'] != null) {
      final Map<Object?, Object?> error = (replyMap['error'] as Map<Object?, Object?>?)!;
      throw PlatformException(
        code: (error['code'] as String?)!,
        message: error['message'] as String?,
        details: error['details'],
      );
    } else {
      return;
    }
  }

  Future<void> loadNextPage() async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.MainModelBridge.loadNextPage', codec, binaryMessenger: _binaryMessenger);
    final Map<Object?, Object?>? replyMap =
        await channel.send(null) as Map<Object?, Object?>?;
    if (replyMap == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyMap['error'] != null) {
      final Map<Object?, Object?> error = (replyMap['error'] as Map<Object?, Object?>?)!;
      throw PlatformException(
        code: (error['code'] as String?)!,
        message: error['message'] as String?,
        details: error['details'],
      );
    } else {
      return;
    }
  }

  Future<void> filter(SnippetFilter arg_filter) async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.MainModelBridge.filter', codec, binaryMessenger: _binaryMessenger);
    final Map<Object?, Object?>? replyMap =
        await channel.send(<Object?>[arg_filter]) as Map<Object?, Object?>?;
    if (replyMap == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyMap['error'] != null) {
      final Map<Object?, Object?> error = (replyMap['error'] as Map<Object?, Object?>?)!;
      throw PlatformException(
        code: (error['code'] as String?)!,
        message: error['message'] as String?,
        details: error['details'],
      );
    } else {
      return;
    }
  }

  Future<void> logOut() async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.MainModelBridge.logOut', codec, binaryMessenger: _binaryMessenger);
    final Map<Object?, Object?>? replyMap =
        await channel.send(null) as Map<Object?, Object?>?;
    if (replyMap == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyMap['error'] != null) {
      final Map<Object?, Object?> error = (replyMap['error'] as Map<Object?, Object?>?)!;
      throw PlatformException(
        code: (error['code'] as String?)!,
        message: error['message'] as String?,
        details: error['details'],
      );
    } else {
      return;
    }
  }

  Future<void> refreshSnippetUpdates() async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.MainModelBridge.refreshSnippetUpdates', codec, binaryMessenger: _binaryMessenger);
    final Map<Object?, Object?>? replyMap =
        await channel.send(null) as Map<Object?, Object?>?;
    if (replyMap == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyMap['error'] != null) {
      final Map<Object?, Object?> error = (replyMap['error'] as Map<Object?, Object?>?)!;
      throw PlatformException(
        code: (error['code'] as String?)!,
        message: error['message'] as String?,
        details: error['details'],
      );
    } else {
      return;
    }
  }
}

class _DetailModelBridgeCodec extends StandardMessageCodec{
  const _DetailModelBridgeCodec();
  @override
  void writeValue(WriteBuffer buffer, Object? value) {
    if (value is DetailModelStateData) {
      buffer.putUint8(128);
      writeValue(buffer, value.encode());
    } else 
    if (value is Owner) {
      buffer.putUint8(129);
      writeValue(buffer, value.encode());
    } else 
    if (value is Snippet) {
      buffer.putUint8(130);
      writeValue(buffer, value.encode());
    } else 
    if (value is SnippetCode) {
      buffer.putUint8(131);
      writeValue(buffer, value.encode());
    } else 
    if (value is SnippetLanguage) {
      buffer.putUint8(132);
      writeValue(buffer, value.encode());
    } else 
    if (value is SyntaxToken) {
      buffer.putUint8(133);
      writeValue(buffer, value.encode());
    } else 
{
      super.writeValue(buffer, value);
    }
  }
  @override
  Object? readValueOfType(int type, ReadBuffer buffer) {
    switch (type) {
      case 128:       
        return DetailModelStateData.decode(readValue(buffer)!);
      
      case 129:       
        return Owner.decode(readValue(buffer)!);
      
      case 130:       
        return Snippet.decode(readValue(buffer)!);
      
      case 131:       
        return SnippetCode.decode(readValue(buffer)!);
      
      case 132:       
        return SnippetLanguage.decode(readValue(buffer)!);
      
      case 133:       
        return SyntaxToken.decode(readValue(buffer)!);
      
      default:      
        return super.readValueOfType(type, buffer);
      
    }
  }
}

class DetailModelBridge {
  /// Constructor for [DetailModelBridge].  The [binaryMessenger] named argument is
  /// available for dependency injection.  If it is left null, the default
  /// BinaryMessenger will be used which routes to the host platform.
  DetailModelBridge({BinaryMessenger? binaryMessenger}) : _binaryMessenger = binaryMessenger;
  final BinaryMessenger? _binaryMessenger;

  static const MessageCodec<Object?> codec = _DetailModelBridgeCodec();

  Future<DetailModelStateData> getState() async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.DetailModelBridge.getState', codec, binaryMessenger: _binaryMessenger);
    final Map<Object?, Object?>? replyMap =
        await channel.send(null) as Map<Object?, Object?>?;
    if (replyMap == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyMap['error'] != null) {
      final Map<Object?, Object?> error = (replyMap['error'] as Map<Object?, Object?>?)!;
      throw PlatformException(
        code: (error['code'] as String?)!,
        message: error['message'] as String?,
        details: error['details'],
      );
    } else if (replyMap['result'] == null) {
      throw PlatformException(
        code: 'null-error',
        message: 'Host platform returned null value for non-null return value.',
      );
    } else {
      return (replyMap['result'] as DetailModelStateData?)!;
    }
  }

  Future<void> load(String arg_uuid) async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.DetailModelBridge.load', codec, binaryMessenger: _binaryMessenger);
    final Map<Object?, Object?>? replyMap =
        await channel.send(<Object?>[arg_uuid]) as Map<Object?, Object?>?;
    if (replyMap == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyMap['error'] != null) {
      final Map<Object?, Object?> error = (replyMap['error'] as Map<Object?, Object?>?)!;
      throw PlatformException(
        code: (error['code'] as String?)!,
        message: error['message'] as String?,
        details: error['details'],
      );
    } else {
      return;
    }
  }

  Future<void> like() async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.DetailModelBridge.like', codec, binaryMessenger: _binaryMessenger);
    final Map<Object?, Object?>? replyMap =
        await channel.send(null) as Map<Object?, Object?>?;
    if (replyMap == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyMap['error'] != null) {
      final Map<Object?, Object?> error = (replyMap['error'] as Map<Object?, Object?>?)!;
      throw PlatformException(
        code: (error['code'] as String?)!,
        message: error['message'] as String?,
        details: error['details'],
      );
    } else {
      return;
    }
  }

  Future<void> dislike() async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.DetailModelBridge.dislike', codec, binaryMessenger: _binaryMessenger);
    final Map<Object?, Object?>? replyMap =
        await channel.send(null) as Map<Object?, Object?>?;
    if (replyMap == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyMap['error'] != null) {
      final Map<Object?, Object?> error = (replyMap['error'] as Map<Object?, Object?>?)!;
      throw PlatformException(
        code: (error['code'] as String?)!,
        message: error['message'] as String?,
        details: error['details'],
      );
    } else {
      return;
    }
  }

  Future<void> save() async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.DetailModelBridge.save', codec, binaryMessenger: _binaryMessenger);
    final Map<Object?, Object?>? replyMap =
        await channel.send(null) as Map<Object?, Object?>?;
    if (replyMap == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyMap['error'] != null) {
      final Map<Object?, Object?> error = (replyMap['error'] as Map<Object?, Object?>?)!;
      throw PlatformException(
        code: (error['code'] as String?)!,
        message: error['message'] as String?,
        details: error['details'],
      );
    } else {
      return;
    }
  }

  Future<void> copyToClipboard() async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.DetailModelBridge.copyToClipboard', codec, binaryMessenger: _binaryMessenger);
    final Map<Object?, Object?>? replyMap =
        await channel.send(null) as Map<Object?, Object?>?;
    if (replyMap == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyMap['error'] != null) {
      final Map<Object?, Object?> error = (replyMap['error'] as Map<Object?, Object?>?)!;
      throw PlatformException(
        code: (error['code'] as String?)!,
        message: error['message'] as String?,
        details: error['details'],
      );
    } else {
      return;
    }
  }

  Future<void> share() async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.DetailModelBridge.share', codec, binaryMessenger: _binaryMessenger);
    final Map<Object?, Object?>? replyMap =
        await channel.send(null) as Map<Object?, Object?>?;
    if (replyMap == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyMap['error'] != null) {
      final Map<Object?, Object?> error = (replyMap['error'] as Map<Object?, Object?>?)!;
      throw PlatformException(
        code: (error['code'] as String?)!,
        message: error['message'] as String?,
        details: error['details'],
      );
    } else {
      return;
    }
  }
}
