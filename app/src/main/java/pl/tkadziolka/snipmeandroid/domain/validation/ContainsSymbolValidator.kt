package pl.tkadziolka.snipmeandroid.domain.validation

import pl.tkadziolka.snipmeandroid.domain.message.ValidationMessages

class ContainsSymbolValidator(
    override val messages: ValidationMessages,
    private val all: Boolean = true,
    vararg symbols: Char
) : Validator(messages) {
    private var _symbols: CharArray = symbols

    override fun validate(phrase: String): ValidationResult {
        val success = if (all) {
            _symbols.all { phrase.contains(it) }
        } else {
            _symbols.any { phrase.contains(it) }
        }

        return if (success) Valid else Invalid("")
    }
}