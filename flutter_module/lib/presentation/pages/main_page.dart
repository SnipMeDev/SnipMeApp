import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/messages.dart';
import 'package:flutter_module/presentation/styles/color_styles.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';
import 'package:flutter_module/presentation/widgets/snippet_list_item.dart';
import 'package:flutter_module/presentation/widgets/view_state_wrapper.dart';
import 'package:flutter_module/utils/extensions/build_context_extensions.dart';
import 'package:flutter_module/utils/hooks/use_observable_state_hook.dart';

class MainPage extends HookWidget {
  const MainPage({
    Key? key,
    required this.model,
  }) : super(key: key);

  final MainModelApi model;

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
    }, []);

    if (event.value.event == MainModelEvent.logout) {
      context.popOrExit();
    }

    return Scaffold(
      appBar: AppBar(title: const Text("SnipMe")),
      backgroundColor: ColorStyles.pageBackground(),
      body: ViewStateWrapper<List<Snippet>>(
        isLoading: data.state == ModelState.loading || data.is_loading == true,
        error: data.error,
        data: data.data?.cast(),
        builder: (_, snippets) {
          return _MainPage(snippets: snippets ?? List.empty());
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => model.loadNextPage(),
        tooltip: 'Increment',
        child: const Icon(Icons.add),
      ), // This trailing comma makes auto-formatting nicer for build methods.
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

class _MainPage extends StatelessWidget {
  const _MainPage({
    Key? key,
    required this.snippets,
  }) : super(key: key);

  final List<Snippet> snippets;

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: snippets.length,
      itemBuilder: (_, index) => Padding(
        padding: const EdgeInsets.symmetric(
          vertical: Dimens.s,
          horizontal: Dimens.m,
        ),
        child: SnippetListTile(
          snippet: snippets[index],
        ),
      ),
    );
  }
}
