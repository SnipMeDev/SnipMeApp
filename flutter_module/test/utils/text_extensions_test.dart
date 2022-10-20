import 'package:flutter_module/utils/extensions/text_extensions.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  group('splitByIndices', () {
    test('Phrase should be split between delimiters', () {
      // Given
      const phrase = "Thisxisxsimplexphrase";
      // When
      final elements = phrase.splitByIndices([0, 4, 5, 7, 8, 14, 15, 21]);
      // Then
      expect(elements, ["This", "x", "is", "x", "simple", "x", "phrase"]);
    });

    test('Phrase should be split between unsorted delimiters', () {
      // Given
      const phrase = "Thisxisxsimplexphrase";
      // When
      final elements = phrase.splitByIndices([4, 0, 5, 7, 8, 15, 21, 14]);
      // Then
      expect(elements, ["This", "x", "is", "x", "simple", "x", "phrase"]);
    });

    test('Phrase before first delimiter should be returned with result', () {
      // Given
      const phrase = "AlsoxThisxisxsimplexphrase";
      // When
      final elements = phrase.splitByIndices([5, 9, 10, 12, 13, 19, 20, 26]);
      // Then
      expect(
        elements,
        ["Alsox", "This", "x", "is", "x", "simple", "x", "phrase"],
      );
    });

    test('Phrase after last delimiter should be returned with result', () {
      // Given
      const phrase = "Thisxisxsimplexphrasexend";
      // When
      final elements = phrase.splitByIndices([0, 4, 5, 7, 8, 14, 15, 21]);
      // Then
      expect(
        elements,
        ["This", "x", "is", "x", "simple", "x", "phrase", "xend"],
      );
    });

    group('lines', () {
      test('Returns string of split lines', () {
        // Given
        const toSplit = 'There\nare\rfour\r\nlines';
        // When
        final result = toSplit.lines(4);
        // Then
        expect(result, 'There\nare\nfour\nlines');
      });

      test('Returns n or less split lines', () {
        // Given
        const toSplit = 'There\nare\rfour\r\nlines';
        // When
        final result = toSplit.lines(3);
        // Then
        expect(result, 'There\nare\nfour');
      });

      test('Returns line without terminator for oneliner', () {
        // Given
        const toSplit = 'Oneliner';
        // When
        final result = toSplit.lines(3);
        // Then
        expect(result, 'Oneliner');
      });
    });
  });
}
