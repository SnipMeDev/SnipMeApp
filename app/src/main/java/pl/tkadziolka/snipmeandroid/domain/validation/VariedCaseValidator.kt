package pl.tkadziolka.snipmeandroid.domain.validation

import pl.tkadziolka.snipmeandroid.domain.message.ValidationMessages

class VariedCaseValidator(override val messages: ValidationMessages) : Validator(messages) {
    override fun validate(phrase: String): ValidationResult {
        val hasLowerCase = phrase.any { it.isLowerCase() }
        val hasUpperCase = phrase.any { it.isUpperCase() }

        if (hasLowerCase && hasUpperCase) return Valid
        return Invalid(messages.phraseNoUpperLower)
    }
}