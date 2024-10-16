import 'package:flutter/material.dart';
import 'package:flutter_module/presentation/styles/color_styles.dart';
import 'package:flutter_module/presentation/styles/dimens.dart';

typedef FilterSelectedListener = Function(String filter, bool selected);

class FilterListView extends StatelessWidget {
  const FilterListView({
    Key? key,
    required this.filters,
    required this.selected,
    this.onSelected,
  }) : super(key: key);

  final List<String?> filters;
  final List<String?> selected;
  final FilterSelectedListener? onSelected;

  @override
  Widget build(BuildContext context) {
    return ListView.separated(
      physics: const BouncingScrollPhysics(),
      itemCount: filters.length,
      scrollDirection: Axis.horizontal,
      separatorBuilder: (_, __) => const SizedBox(width: Dimens.m,),
      itemBuilder: (BuildContext context, int index) {
        final filter = filters[index];
        return ChoiceChip(
          disabledColor: ColorStyles.filterBackgroundColor().withOpacity(0.08),
          selectedColor: ColorStyles.filterBackgroundColor().withOpacity(0.25),
          label: Text(filter ?? ''),
          selected: selected.contains(filter),
          onSelected: (isSelected) => onSelected?.call(filter ?? '', isSelected),
        );
      },
    );
  }
}
