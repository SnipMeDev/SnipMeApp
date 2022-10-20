package pl.tkadziolka.snipmeandroid.domain.validation

import pl.tkadziolka.snipmeandroid.domain.message.ValidationMessages

class BlankValidator(messages: ValidationMessages): MultiValidator(messages) {
    override fun validate(vararg phrases: String): ValidationResult =
        if (phrases.any { it.isBlank() }) Invalid(messages.phrasesBlank) else Valid
}