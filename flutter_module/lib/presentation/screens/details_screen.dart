import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/model/main_model.dart';
import 'package:flutter_module/presentation/navigation/details/details_navigator.dart';
import 'package:flutter_module/presentation/screens/named_screen.dart';
import 'package:flutter_module/presentation/styles/color_styles.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/styles/padding_styles.dart';
import 'package:flutter_module/presentation/widgets/code_text_view.dart';
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
      snippetId: navigator.snippetId!,
    );
  }
}

class _DetailsPage extends HookWidget {
  const _DetailsPage({
    Key? key,
    required this.navigator,
    required this.model,
    required this.snippetId,
  }) : super(key: key);

  final DetailsNavigator navigator;
  final DetailModelBridge model;
  final String snippetId;

  @override
  Widget build(BuildContext context) {
    useNavigator([navigator]);

    final stateChange = useObservableState(
      DetailModelStateData(),
      () => model.getState(),
      (current, newState) => (current as DetailModelStateData).equals(newState),
    );

    final state = stateChange.value;

    useEffect(() {
      model.load(snippetId);
      return null;
    }, []);

    // TODO Add view state wrapper and show error for null snippet
    return Scaffold(
      backgroundColor: ColorStyles.surfacePrimary(),
      appBar: AppBar(
        title: Text(state.data!.title ?? 'Details'),
        backgroundColor: ColorStyles.surfacePrimary(),
        foregroundColor: Colors.black,
        elevation: 0,
        leading: BackButton(
          onPressed: navigator.back,
          color: Colors.black,
        ),
        actions: state.data?.isPrivate == true
            ? [const PaddingStyles.regular(Icon(Icons.visibility_off_outlined))]
            : null,
      ),
      body: ViewStateWrapper<Snippet>(
        isLoading: state.state == ModelState.loading || state.is_loading == true,
        error: state.error,
        data: state.data,
        builder: (_, snippet) => _DetailPageData(
          model: model,
          state: state,
        ),
      ),
    );
  }
}

class _DetailPageData extends StatelessWidget {
  const _DetailPageData({
    Key? key,
    required this.model,
    required this.state,
  }) : super(key: key);

  final DetailModelBridge model;
  final DetailModelStateData state;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        PaddingStyles.regular(SnippetDetailsBar(snippet: state.data!)),
        Expanded(
          child: ColoredBox(
            color: ColorStyles.codeBackground(),
            child: NotificationListener<OverscrollIndicatorNotification>(
              onNotification: (overScroll) {
                overScroll.disallowIndicator();
                return true;
              },
              child: SingleChildScrollView(
                padding: const EdgeInsets.all(Dimens.l),
                physics: const ClampingScrollPhysics(),
                child: CodeTextView(
                  code: state.data!.code!.raw!,
                  tokens: state.data?.code?.tokens,
                ),
              ),
            ),
          ),
        ),
        PaddingStyles.regular(
          Center(
            child: SnippetActionBar(
              snippet: state.data!,
              onLikeTap: model.like,
              onDislikeTap: model.dislike,
              onSaveTap: model.save,
              onCopyTap: model.copyToClipboard,
              onShareTap: model.share,
            ),
          ),
        ),
      ],
    );
  }
}
