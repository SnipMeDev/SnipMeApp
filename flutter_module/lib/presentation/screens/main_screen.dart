import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/generated/assets.dart';
import 'package:flutter_module/generated/data_model.g.dart';
import 'package:flutter_module/presentation/navigation/details/details_navigator.dart';
import 'package:flutter_module/presentation/navigation/login/login_navigator.dart';
import 'package:flutter_module/presentation/providers/main_page_state_provider.dart';
import 'package:flutter_module/presentation/screens/named_screen.dart';
import 'package:flutter_module/presentation/styles/color_styles.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/styles/padding_styles.dart';
import 'package:flutter_module/presentation/styles/text_styles.dart';
import 'package:flutter_module/presentation/widgets/filter_dropdown.dart';
import 'package:flutter_module/presentation/widgets/filter_list_view.dart';
import 'package:flutter_module/presentation/widgets/snippet_list_item.dart';
import 'package:flutter_module/presentation/widgets/view_state_wrapper.dart';
import 'package:flutter_module/utils/hooks/use_navigator.dart';
import 'package:go_router_plus/go_router_plus.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';

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
  Widget build(BuildContext context, GoRouterState state) {
    return _MainPage(
      loginNavigator: loginNavigator,
      detailsNavigator: detailsNavigator,
      model: model,
    );
  }
}

class _MainPage extends HookConsumerWidget {
  const _MainPage({
    required this.loginNavigator,
    required this.detailsNavigator,
    required this.model,
  });

  final LoginNavigator loginNavigator;
  final DetailsNavigator detailsNavigator;
  final MainModelBridge model;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    useNavigator([loginNavigator, detailsNavigator]);
    final expandedState = useState(true);
    final controller = useScrollController();

    final stateNotification = ref.watch(mainPageStateProvider);
    final state = stateNotification.state;
    final eventNotification = ref.watch(mainPageEventProvider);
    final event = eventNotification.event;

    useEffect(() {
      model.initState();
      return null;
    }, []);

    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (event == MainModelEvent.logout) {
        model.resetEvent();
        loginNavigator.logout();
      }
    });

    return Scaffold(
      backgroundColor: ColorStyles.pageBackground(),
      body: ViewStateWrapper<List<Snippet>>(
        isLoading: state == ModelState.loading || stateNotification.isLoading == true,
        error: stateNotification.error,
        data: stateNotification.data?.cast(),
        builder: (_, snippets) {
          return _MainPageData(
            navigator: detailsNavigator,
            model: model,
            snippets: snippets ?? List.empty(),
            filter: stateNotification.filter ?? SnippetFilter(),
            controller: controller,
            expanded: expandedState.value,
            onExpandChange: (expanded) => expandedState.value = expanded,
          );
        },
      ),
      floatingActionButton: FloatingActionButton.small(
        onPressed: () {
          controller.animateTo(
            0.0,
            duration: const Duration(seconds: 1),
            curve: Curves.easeIn,
          );
        },
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

typedef ExpandChangeListener = Function(bool);

class _MainPageData extends HookWidget {
  const _MainPageData(
      {required this.navigator,
      required this.model,
      required this.snippets,
      required this.filter,
      required this.controller,
      required this.expanded,
      required this.onExpandChange});

  final DetailsNavigator navigator;
  final MainModelBridge model;
  final List<Snippet> snippets;
  final SnippetFilter filter;
  final ScrollController controller;
  final bool expanded;
  final ExpandChangeListener onExpandChange;

  @override
  Widget build(BuildContext context) {
    return NestedScrollView(
      controller: controller,
      floatHeaderSlivers: true,
      headerSliverBuilder: (_, __) {
        return [
          SliverAppBar(
            elevation: 0.0,
            centerTitle: true,
            title: Row(mainAxisSize: MainAxisSize.min, children: [
              Image.asset(Assets.appLogo, width: Dimens.logoSignetSize),
              const SizedBox(width: Dimens.m),
              const TextStyles.appBarLogo('SnipMe'),
            ]),
            backgroundColor: ColorStyles.surfacePrimary(),
            leading: IconButton(
              icon: const Icon(Icons.logout),
              color: Colors.black,
              onPressed: model.logOut,
            ),
            actions: [
              IconButton(
                icon: Icon(
                  expanded
                      ? Icons.close_fullscreen_outlined
                      : Icons.open_in_full_outlined,
                ),
                color: Colors.black,
                onPressed: () => onExpandChange(!expanded),
              ),
            ],
          ),
          SliverAppBar(
              floating: true,
              forceElevated: true,
              expandedHeight: Dimens.extendedAppBarHeight,
              elevation: Dimens.s / 2,
              backgroundColor: ColorStyles.surfacePrimary(),
              shape: const RoundedRectangleBorder(
                borderRadius: BorderRadius.only(
                  bottomLeft: Radius.circular(Dimens.l),
                  bottomRight: Radius.circular(Dimens.l),
                ),
              ),
              flexibleSpace: FlexibleSpaceBar(
                collapseMode: CollapseMode.parallax,
                background: Padding(
                  padding: const EdgeInsets.symmetric(horizontal: Dimens.m),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.end,
                    children: [
                      PaddingStyles.small(
                        Row(children: [TextStyles.bold("Scope")]),
                      ),
                      PaddingStyles.small(
                        Row(
                          children: [
                            Expanded(
                              child: SizedBox(
                                height: Dimens.filterDropdownHeight,
                                child: FilterDropdown(
                                  filters: filter.scopes ?? List.empty(),
                                  selected: filter.selectedScope ?? '',
                                  onSelected: (scope) {
                                    model.filterScope(scope);
                                  },
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                      PaddingStyles.small(
                        Row(children: [TextStyles.bold("Language")]),
                      ),
                      SizedBox(
                        height: Dimens.filterListHeight,
                        child: FilterListView(
                          filters: filter.languages ?? List.empty(),
                          selected: filter.selectedLanguages ?? List.empty(),
                          onSelected: (language, isSelected) {
                            model.filterLanguage(language, isSelected);
                          },
                        ),
                      ),
                      const SizedBox(
                        height: Dimens.m,
                      )
                    ],
                  ),
                ),
              ))
        ];
      },
      body: CustomScrollView(
        scrollBehavior: const ScrollBehavior(),
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
                      isExpanded: expanded,
                      snippet: snippet,
                      onTap: () {
                        navigator.goToDetails(context, snippet.uuid!);
                      },
                    ),
                  );
                },
              )
            ]),
          ),
        ],
      ),
    );
  }
}
