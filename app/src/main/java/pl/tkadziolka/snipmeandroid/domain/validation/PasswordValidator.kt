package pl.tkadziolka.snipmeandroid.domain.validation

import pl.tkadziolka.snipmeandroid.domain.message.ValidationMessages

class PasswordValidator(messages: ValidationMessages) : Validator(messages) {
    private val lengthValidator = LengthValidator(messages, min = MIN_LENGTH)
    private val variedCaseValidator = VariedCaseValidator(messages)
    private val specialCharValidator = ContainsSymbolValidator(
        messages = messages,
        all = false,
        symbols = *SPECIAL_CHARS.toCharArray()
    )
    private val containsDigitValidator = ContainsSymbolValidator(
        messages = messages,
        all = false,
        symbols = *DIGITS.toCharArray()
    )

    override fun validate(phrase: String): ValidationResult {
        lengthValidator.validate(phrase).messageOrNull()
            ?.let { return Invalid(messages.passwordTooShort) }

        variedCaseValidator.validate(phrase).messageOrNull()
            ?.let { return Invalid(messages.passwordNoUpperLower) }

        specialCharValidator.validate(phrase).messageOrNull()
            ?.let { return Invalid(messages.passwordNoSpecialChar) }

        containsDigitValidator.validate(phrase).messageOrNull()
            ?.let { return Invalid(messages.passwordNoDigit) }

        return Valid
    }

    companion object {
        const val MIN_LENGTH = 8
        const val SPECIAL_CHARS = "!@#$%^&*()_+=-{}:;<>/`"
        const val DIGITS = "1234567890"
    }
}