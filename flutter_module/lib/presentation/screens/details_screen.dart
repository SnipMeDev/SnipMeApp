import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/generated/data_model.g.dart';
import 'package:flutter_module/presentation/navigation/details/details_navigator.dart';
import 'package:flutter_module/presentation/providers/details_page_state_provider.dart';
import 'package:flutter_module/presentation/screens/named_screen.dart';
import 'package:flutter_module/presentation/styles/color_styles.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/styles/padding_styles.dart';
import 'package:flutter_module/presentation/widgets/code_text_view.dart';
import 'package:flutter_module/presentation/widgets/no_overscroll_single_child_scroll_view.dart';
import 'package:flutter_module/presentation/widgets/snippet_action_bar.dart';
import 'package:flutter_module/presentation/widgets/snippet_details_bar.dart';
import 'package:flutter_module/presentation/widgets/view_state_wrapper.dart';
import 'package:flutter_module/utils/hooks/use_navigator.dart';
import 'package:go_router/go_router.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:screenshot/screenshot.dart';

class DetailsScreen extends NamedScreen {
  DetailsScreen({
    required this.navigator,
    required this.model,
  }) : super(name);

  static String name = 'details';

  final DetailsNavigator navigator;
  final ChannelDetailsModel model;

  @override
  Widget build(BuildContext context, GoRouterState state) {
    return _DetailsPage(
      navigator: navigator,
      model: model,
    );
  }
}

class _DetailsPage extends HookConsumerWidget {
  const _DetailsPage({
    required this.navigator,
    required this.model,
  });

  final DetailsNavigator navigator;
  final ChannelDetailsModel model;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final captureController = ScreenshotController();
    useNavigator([navigator]);

    final stateNotification = ref.watch(detailsPageStateProvider);
    final state = stateNotification.state;
    final eventNotification = ref.watch(detailsPageEventProvider);
    final event = eventNotification.event;

    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (event == DetailsModelEvent.saved) {
        final snippetId = eventNotification.value;
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
      if (event == DetailsModelEvent.deleted) {
        _exit();
      }
    });

    void saveImage() {
      captureController.capture().then((image) {
        if (image == null) return;
        model.saveImage(image);
      });
    }

    void shareImage() {
      captureController.capture().then((image) {
        if (image == null) return;
        model.shareImage(image);
      });
    }

    useEffect(() {
      model.load(navigator.snippetId ?? '');
      return null;
    }, []);

    return Scaffold(
      backgroundColor: ColorStyles.surfacePrimary(),
      appBar: AppBar(
        title: Text(stateNotification.data?.title ?? ''),
        backgroundColor: ColorStyles.surfacePrimary(),
        foregroundColor: Colors.black,
        elevation: 0,
        leading: BackButton(
          onPressed: navigator.back,
          color: Colors.black,
        ),
        actions: stateNotification.data?.isPrivate == true
            ? [const PaddingStyles.regular(Icon(Icons.lock_outlined))]
            : null,
      ),
      body: ViewStateWrapper<Snippet>(
        isLoading:
            state == ModelState.loading || stateNotification.isLoading == true,
        error: stateNotification.error,
        data: stateNotification.data as Snippet?,
        builder: (_, snippet) => _DetailPageData(
          model: model,
          snippet: snippet,
          captureController: captureController,
          saveImage: saveImage,
          shareImage: shareImage,
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
    required this.model,
    required this.snippet,
    required this.captureController,
    required this.saveImage,
    required this.shareImage,
  });

  final ChannelDetailsModel model;
  final Snippet? snippet;
  final ScreenshotController captureController;
  final VoidCallback saveImage;
  final VoidCallback shareImage;

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
              child: Screenshot(
                controller: captureController,
                child: CodeTextView(
                  code: snippet!.code!.raw!,
                  tokens: snippet!.code?.tokens,
                ),
              ),
            ),
          ),
        ),
        PaddingStyles.regular(
          Center(
            child: SnippetActionBar(
              snippet: snippet!,
              onFavoriteTap: model.favorite,
              onSaveTap: saveImage,
              onCopyTap: model.copyToClipboard,
              onShareTap: shareImage,
              onDeleteTap: model.delete,
            ),
          ),
        ),
      ],
    );
  }
}
