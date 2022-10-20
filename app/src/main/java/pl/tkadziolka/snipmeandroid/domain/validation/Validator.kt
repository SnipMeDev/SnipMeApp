package pl.tkadziolka.snipmeandroid.domain.validation

import pl.tkadziolka.snipmeandroid.domain.message.ValidationMessages

abstract class Validator(open val messages: ValidationMessages) {
    abstract fun validate(phrase: String): ValidationResult
}

abstract class MultiValidator(open val messages: ValidationMessages) {
    abstract fun validate(vararg phrases: String): ValidationResult
}

fun ValidationResult.messageOrNull(): String? = when(this) {
    is Invalid -> this.message
    else -> null
}

sealed class ValidationResult
object Valid: ValidationResult()
data class Invalid(val message: String): ValidationResult()