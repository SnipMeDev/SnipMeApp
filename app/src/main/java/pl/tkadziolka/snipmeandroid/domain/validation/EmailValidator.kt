package pl.tkadziolka.snipmeandroid.domain.validation

import pl.tkadziolka.snipmeandroid.domain.message.ValidationMessages

class EmailValidator(messages: ValidationMessages): Validator(messages) {
    private val lengthValidator = LengthValidator(messages, min = MIN_LENGTH)
    private val structureValidator = ContainsSymbolValidator(
        messages = messages,
        symbols = *SYMBOLS.toCharArray()
    )

    override fun validate(phrase: String): ValidationResult {
        lengthValidator.validate(phrase).messageOrNull()
            ?.let { return Invalid(messages.emailTooShort) }

        structureValidator.validate(phrase).messageOrNull()
            ?.let { return Invalid(messages.emailWrongStructure) }

        return Valid
    }

    companion object {
        const val MIN_LENGTH = 5
        private val SYMBOLS = arrayOf('@', '.')
    }
}