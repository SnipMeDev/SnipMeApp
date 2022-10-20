package pl.tkadziolka.snipmeandroid.domain.validation

import pl.tkadziolka.snipmeandroid.domain.message.ValidationMessages

class EqualsValidator(messages: ValidationMessages) : MultiValidator(messages) {
    override fun validate(vararg phrases: String): ValidationResult =
        if (phrases.all { it == phrases.first() }) {
            Valid
        } else {
            Invalid(messages.phrasesNotEqual)
        }
}