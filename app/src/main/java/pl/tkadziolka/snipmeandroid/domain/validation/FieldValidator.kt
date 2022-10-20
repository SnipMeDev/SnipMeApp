package pl.tkadziolka.snipmeandroid.domain.validation

import pl.tkadziolka.snipmeandroid.domain.message.ValidationMessages

class FieldValidator(
    messages: ValidationMessages
) : LengthValidator(messages, min = MIN_LENGTH) {
    companion object {
        const val MIN_LENGTH = 1
    }
}