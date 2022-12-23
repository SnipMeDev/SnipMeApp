import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/model/main_model.dart';
import 'package:flutter_module/presentation/navigation/details/details_navigator.dart';
import 'package:flutter_module/presentation/screens/named_screen.dart';
import 'package:flutter_module/presentation/styles/color_styles.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/styles/padding_styles.dart';
import 'package:flutter_module/presentation/widgets/code_text_view.dart';
import 'package:flutter_module/presentation/widgets/no_overscroll_single_child_scroll_view.dart';
import 'package:flutter_module/presentation/widgets/snippet_action_bar.dart';
import 'package:flutter_module/presentation/widgets/snippet_details_bar.dart';
import 'package:flutter_module/presentation/widgets/view_state_wrapper.dart';
import 'package:flutter_module/utils/extensions/state_extensions.dart';
import 'package:flutter_module/utils/hooks/use_navigator.dart';
import 'package:flutter_module/utils/hooks/use_observable_state_hook.dart';
import 'package:go_router/go_router.dart';

class DetailsScreen extends NamedScreen {
  DetailsScreen({
    required this.navigator,
    required this.model,
  }) : super(name);

  static String name = 'details';

  final DetailsNavigator navigator;
  final DetailModelBridge model;

  @override
  Widget builder(BuildContext context, GoRouterState state) {
    return _DetailsPage(
      navigator: navigator,
      model: model,
    );
  }
}

class _DetailsPage extends HookWidget {
  const _DetailsPage({
    Key? key,
    required this.navigator,
    required this.model,
  }) : super(key: key);

  final DetailsNavigator navigator;
  final DetailModelBridge model;

  @override
  Widget build(BuildContext context) {
    useNavigator([navigator]);

    final state = useObservableState(
      DetailModelStateData(),
      () => model.getState(),
      (current, newState) => (current as DetailModelStateData).equals(newState),
    ).value;

    final event = useObservableState(
      DetailModelEventData(),
      () => model.getEvent(),
      (current, newState) => (current as DetailModelEventData).equals(newState),
    ).value;

    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (event.event == DetailModelEvent.saved) {
        final snippetId = event.value;
        if (snippetId == null) {
          _exit();
          return;
        }

        _exit();
        WidgetsBinding.instance.addPostFrameCallback((_) {
          navigator.goToDetails(context, snippetId);
        });
      }
    });

    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (event.event == DetailModelEvent.deleted) {
        _exit();
      }
    });

    useEffect(() {
      model.load(navigator.snippetId ?? '');
      return null;
    }, []);

    return Scaffold(
      backgroundColor: ColorStyles.surfacePrimary(),
      appBar: AppBar(
        title: Text(state.data?.title ?? ''),
        backgroundColor: ColorStyles.surfacePrimary(),
        foregroundColor: Colors.black,
        elevation: 0,
        leading: BackButton(
          onPressed: navigator.back,
          color: Colors.black,
        ),
        actions: state.data?.isPrivate == true
            ? [const PaddingStyles.regular(Icon(Icons.lock_outlined))]
            : null,
      ),
      body: ViewStateWrapper<Snippet>(
        isLoading:
            state.state == ModelState.loading || state.is_loading == true,
        error: state.error,
        data: state.data,
        builder: (_, snippet) => _DetailPageData(
          model: model,
          snippet: snippet,
        ),
      ),
    );
  }

  void _exit() {
    model.resetEvent();
    navigator.back();
  }
}

class _DetailPageData extends StatelessWidget {
  const _DetailPageData({
    Key? key,
    required this.model,
    required this.snippet,
  }) : super(key: key);

  final DetailModelBridge model;
  final Snippet? snippet;

  @override
  Widget build(BuildContext context) {
    if (snippet == null) return const SizedBox();

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        PaddingStyles.regular(SnippetDetailsBar(snippet: snippet!)),
        Expanded(
          child: ColoredBox(
            color: ColorStyles.codeBackground(),
            child: NoOverscrollSingleChildScrollView(
              padding: const EdgeInsets.all(Dimens.l),
              child: CodeTextView(
                code: snippet!.code!.raw!,
                tokens: snippet!.code?.tokens,
              ),
            ),
          ),
        ),
        PaddingStyles.regular(
          Center(
            child: SnippetActionBar(
              snippet: snippet!,
              onLikeTap: model.like,
              onDislikeTap: model.dislike,
              onSaveTap: model.save,
              onCopyTap: model.copyToClipboard,
              onShareTap: model.share,
              onDeleteTap: model.delete,
            ),
          ),
        ),
      ],
    );
  }
}
