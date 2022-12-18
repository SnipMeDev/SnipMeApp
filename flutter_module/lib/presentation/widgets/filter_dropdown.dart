import 'package:flutter/material.dart';

typedef FilterSelectedItemListener = Function(String);

class FilterDropdown extends StatelessWidget {
  const FilterDropdown({
    Key? key,
    required this.filters,
    required this.selected,
    this.onSelected,
  }) : super(key: key);

  final List<String?> filters;
  final String selected;
  final FilterSelectedItemListener? onSelected;

  @override
  Widget build(BuildContext context) {
    return DropdownButtonHideUnderline(
      child: DropdownButton<String>(
        onChanged: (filter) => onSelected?.call(filter ?? ''),
        value: selected,
        items: [
          // TODO Hardcoded list works, why?
          ...filters.map(
            (filter) => DropdownMenuItem(
              child: Text(filter ?? ''),
            ),
          )
        ],
      ),
    );
  }
}
