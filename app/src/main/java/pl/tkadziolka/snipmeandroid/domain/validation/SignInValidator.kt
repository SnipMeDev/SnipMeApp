package pl.tkadziolka.snipmeandroid.domain.validation

import pl.tkadziolka.snipmeandroid.domain.message.ValidationMessages

// TODO: 20/02/2021 Create unit test
class SignInValidator(private val messages: ValidationMessages) {

    fun validate(login: String, password: String): ValidationResult {

        BlankValidator(messages).validate(login, password).messageOrNull()?.let { message ->
            return Invalid(message)
        }

        FieldValidator(messages).validate(login).messageOrNull()?.let { message ->
            return Invalid(message)
        }

        PasswordValidator(messages).validate(password).messageOrNull()?.let { message ->
            return Invalid(message)
        }

        return Valid
    }
}