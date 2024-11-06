import 'package:flutter/material.dart';

typedef DataWidgetBuilder<T> = Widget Function(BuildContext context, T? data);

class ViewStateWrapper<T> extends StatelessWidget {
  const ViewStateWrapper({
    super.key,
    required this.builder,
    this.isLoading = false,
    this.data,
    this.error,
  });

  final bool isLoading;
  final T? data;
  final String? error;
  final DataWidgetBuilder<T> builder;

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        // Data
        Visibility(
          visible: error == null && !isLoading,
          child: builder.call(context, data),
        ),
        // Loading
        Visibility(
          visible: isLoading,
          child: const Center(child: CircularProgressIndicator()),
        ),
        // Error
        Visibility(
          visible: error != null,
          child: Center(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                const Text("Error"),
                Text(error ?? ""),
              ],
            ),
          ),
        )
      ],
    );
  }
}
