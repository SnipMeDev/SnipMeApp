import 'package:flutter/material.dart';
import 'package:flutter_module/presentation/styles/text_styles.dart';

typedef FilterSelectedItemListener = Function(String);

class FilterDropdown extends StatelessWidget {
  const FilterDropdown({
    super.key,
    required this.filters,
    required this.selected,
    this.onSelected,
  });

  final List<String?> filters;
  final String selected;
  final FilterSelectedItemListener? onSelected;

  @override
  Widget build(BuildContext context) {
    return DropdownButtonHideUnderline(
      child: DropdownButton<String>(
        isExpanded: true,
        onChanged: (filter) => onSelected?.call(filter ?? ''),
        value: selected,
        items: [
          ...filters.map(
            (filter) => DropdownMenuItem<String>(
              value: filter,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const SizedBox(width: 24,),
                  TextStyles.title(filter ?? ''),
                ],
              ),
            ),
          )
        ],
      ),
    );
  }
}
