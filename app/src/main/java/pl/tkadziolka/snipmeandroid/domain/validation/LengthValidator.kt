package pl.tkadziolka.snipmeandroid.domain.validation

import pl.tkadziolka.snipmeandroid.domain.message.ValidationMessages

open class LengthValidator(
    override val messages: ValidationMessages,
    private val min: Int = 1,
    private val max: Int = Int.MAX_VALUE
): Validator(messages) {
    override fun validate(phrase: String): ValidationResult =
        when {
            phrase.length < min -> Invalid(messages.phraseTooShort)
            phrase.length > max -> Invalid(messages.phraseTooLong)
            else -> Valid
        }
}