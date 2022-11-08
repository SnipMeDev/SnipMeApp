import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_module/presentation/navigation/details/details_navigator.dart';
import 'package:flutter_module/presentation/screens/named_screen.dart';
import 'package:go_router/go_router.dart';

class DetailsScreen extends NamedScreen {
  DetailsScreen(this.navigator) : super(name);

  static String name = 'details';

  final DetailsNavigator navigator;

  @override
  Widget builder(BuildContext context, GoRouterState state) {
    return _DetailsPage(navigator: navigator);
  }
}

class _DetailsPage extends StatelessWidget {
  const _DetailsPage({
    Key? key,
    required this.navigator,
  }) : super(key: key);

  final DetailsNavigator navigator;

  @override
  Widget build(BuildContext context) {

    useEffect(() {
      navigator.setRouter(GoRouter.of(context));
    }, []);

    return Scaffold(
      appBar: AppBar(
        leading: BackButton(onPressed: navigator.back),
      ),
      body: Center(
        child: Text(DetailsScreen.name),
      ),
    );
  }
}
