import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/generated/assets.dart';
import 'package:flutter_module/model/main_model.dart';
import 'package:flutter_module/presentation/navigation/details/details_navigator.dart';
import 'package:flutter_module/presentation/navigation/login/login_navigator.dart';
import 'package:flutter_module/presentation/screens/named_screen.dart';
import 'package:flutter_module/presentation/styles/color_styles.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/styles/text_styles.dart';
import 'package:flutter_module/presentation/widgets/filter_list_view.dart';
import 'package:flutter_module/presentation/widgets/snippet_list_item.dart';
import 'package:flutter_module/presentation/widgets/view_state_wrapper.dart';
import 'package:flutter_module/utils/extensions/state_extensions.dart';
import 'package:flutter_module/utils/hooks/use_navigator.dart';
import 'package:flutter_module/utils/hooks/use_observable_state_hook.dart';
import 'package:go_router/go_router.dart';
import 'package:go_router/src/state.dart';
import 'package:go_router_plus/go_router_plus.dart';

class MainScreen extends NamedScreen implements UserScreen {
  MainScreen({
    required this.loginNavigator,
    required this.detailsNavigator,
    required this.model,
  }) : super(name);

  static String name = 'main';
  final LoginNavigator loginNavigator;
  final DetailsNavigator detailsNavigator;
  final MainModelBridge model;

  @override
  Widget builder(BuildContext context, GoRouterState state) {
    return _MainPage(
      loginNavigator: loginNavigator,
      detailsNavigator: detailsNavigator,
      model: model,
    );
  }
}

class _MainPage extends HookWidget {
  const _MainPage({
    Key? key,
    required this.loginNavigator,
    required this.detailsNavigator,
    required this.model,
  }) : super(key: key);

  final LoginNavigator loginNavigator;
  final DetailsNavigator detailsNavigator;
  final MainModelBridge model;

  @override
  Widget build(BuildContext context) {
    useNavigator([loginNavigator, detailsNavigator]);
    final state = useObservableState(
      MainModelStateData(),
      () => model.getState(),
      (current, newState) => (current as MainModelStateData).equals(newState),
    ).value;

    // Event
    final event = useObservableState(
      MainModelEventData(),
      () => model.getEvent(),
      (current, newState) => (current as MainModelEventData).equals(newState),
    ).value;

    useEffect(() {
      model.initState();
    }, []);

    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (event.event == MainModelEvent.logout) {
        model.resetEvent();
        loginNavigator.logout();
      }
    });

    return Scaffold(
      backgroundColor: ColorStyles.pageBackground(),
      body: ViewStateWrapper<List<Snippet>>(
        isLoading:
            state.state == ModelState.loading || state.is_loading == true,
        error: state.error,
        data: state.data?.cast(),
        builder: (_, snippets) {
          return _MainPageData(
            navigator: detailsNavigator,
            model: model,
            snippets: snippets ?? List.empty(),
          );
        },
      ),
      floatingActionButton: FloatingActionButton.small(
        onPressed: () => model.loadNextPage(),
        tooltip: 'Scroll to top',
        backgroundColor: ColorStyles.surfacePrimary(),
        child: const Icon(
          Icons.arrow_upward_outlined,
          color: Colors.black,
        ),
      ),
    );
  }
}

class _MainPageData extends HookWidget {
  const _MainPageData({
    Key? key,
    required this.navigator,
    required this.model,
    required this.snippets,
  }) : super(key: key);

  final DetailsNavigator navigator;
  final MainModelBridge model;
  final List<Snippet> snippets;

  @override
  Widget build(BuildContext context) {
    return NestedScrollView(
      floatHeaderSlivers: true,
      headerSliverBuilder: (_, __) {
        return [
          SliverAppBar(
            elevation: 0.0,
            centerTitle: true,
            title: Row(mainAxisSize: MainAxisSize.min, children: [
              Image.asset(Assets.appLogo, width: 18.0),
              const SizedBox(width: Dimens.m),
              TextStyles.appBarLogo('SnipMe'),
            ]),
            backgroundColor: ColorStyles.surfacePrimary(),
            leading: IconButton(
              icon: const Icon(Icons.logout),
              color: Colors.black,
              onPressed: model.logOut,
            ),
            actions: [
              IconButton(
                icon: const Icon(Icons.close_fullscreen_outlined),
                color: Colors.black,
                onPressed: () {
                  // TODO Handle collapse items
                },
              ),
            ],
          ),
          SliverAppBar(
              floating: true,
              expandedHeight: 120,
              elevation: Dimens.m,
              backgroundColor: ColorStyles.surfacePrimary(),
              shape: const RoundedRectangleBorder(
                borderRadius: BorderRadius.only(
                  bottomLeft: Radius.circular(Dimens.l),
                  bottomRight: Radius.circular(Dimens.l),
                ),
              ),
              flexibleSpace: FlexibleSpaceBar(
                collapseMode: CollapseMode.parallax,
                background: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Row(children: [const Text("Language")]),
                    SizedBox(
                      height: 64,
                      child: FilterListView(
                        filters: ['a', 'b' * 200],
                        selected: ['a'],
                      ),
                    ),
                  ],
                ),
              ))
        ];
      },
      body: CustomScrollView(
        scrollBehavior: const ScrollBehavior(
          androidOverscrollIndicator: AndroidOverscrollIndicator.stretch,
        ),
        slivers: [
          SliverList(
            delegate: SliverChildListDelegate([
              ...snippets.map(
                (snippet) {
                  return Padding(
                    padding: const EdgeInsets.symmetric(
                      vertical: Dimens.s,
                      horizontal: Dimens.m,
                    ),
                    child: SnippetListTile(
                      snippet: snippet,
                      onTap: () {
                        navigator.goToDetails(context, snippet.uuid!);
                      },
                    ),
                  );
                },
              ).toList()
            ]),
          ),
        ],
      ),
    );
  }
}
