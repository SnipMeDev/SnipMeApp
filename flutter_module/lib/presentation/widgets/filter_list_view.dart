import 'package:flutter/material.dart';

class FilterListView extends StatelessWidget {
  const FilterListView({
    Key? key,
    required this.filters,
    required this.selected,
  }) : super(key: key);

  final List<String> filters;
  final List<String> selected;

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: filters.length,
      scrollDirection: Axis.horizontal,
      itemBuilder: (BuildContext context, int index) {
        final filter = filters[index];
        return ChoiceChip(
          label: Text(filter),
          selected: selected.contains(filter),
        );
      },
    );
  }
}
