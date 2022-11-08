import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/model/main_model.dart';
import 'package:flutter_module/presentation/navigation/details/details_navigator.dart';
import 'package:flutter_module/presentation/navigation/login/login_navigator.dart';
import 'package:flutter_module/presentation/screens/named_screen.dart';
import 'package:flutter_module/presentation/styles/color_styles.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/widgets/snippet_list_item.dart';
import 'package:flutter_module/presentation/widgets/view_state_wrapper.dart';
import 'package:flutter_module/utils/extensions/build_context_extensions.dart';
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
    final state = useObservableState(
      MainModelStateData(),
      () => model.getState(),
      (oldState, newState) => _equalState(oldState, newState),
    );

    final data = state.value;

    // Event
    final event = useState(MainModelEventData());

    print("Loading = ${data.is_loading}");

    useEffect(() {
      model.initState();
      loginNavigator.setRouter(GoRouter.of(useContext()));
      detailsNavigator.setRouter(GoRouter.of(useContext()));
    }, []);

    if (event.value.event == MainModelEvent.logout) {
      loginNavigator.logout();
    }

    return Scaffold(
      appBar: AppBar(
        leading: Icon(Icons.logout),
        title: const Text("SnipMe"),
      ),
      backgroundColor: ColorStyles.pageBackground(),
      body: ViewStateWrapper<List<Snippet>>(
        isLoading: data.state == ModelState.loading || data.is_loading == true,
        error: data.error,
        data: data.data?.cast(),
        builder: (_, snippets) {
          return _MainPageData(
            navigator: detailsNavigator,
            snippets: snippets ?? List.empty(),
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => model.loadNextPage(),
        tooltip: 'Increment',
        child: const Icon(Icons.add),
      ),
    );
  }

  bool _equalState(Object oldState, Object newState) {
    final oldData = (oldState as MainModelStateData);
    final newData = (newState as MainModelStateData);

    return oldData.state == newState.state &&
        oldData.is_loading == newData.is_loading &&
        oldData.error == newData.error;
  }
}

class _MainPageData extends StatelessWidget {
  const _MainPageData({
    Key? key,
    required this.navigator,
    required this.snippets,
  }) : super(key: key);

  final DetailsNavigator navigator;
  final List<Snippet> snippets;

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: snippets.length,
      itemBuilder: (_, index) {
        final snippet = snippets[index];
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
    );
  }
}
