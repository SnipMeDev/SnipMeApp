package pl.tkadziolka.snipmeandroid.domain.validation

import pl.tkadziolka.snipmeandroid.domain.message.ValidationMessages

// TODO: 20/02/2021 Create unit test
class SignUpValidator(private val messages: ValidationMessages) {
    fun validate(
        login: String,
        password: String,
        repeatedPassword: String,
        email: String
    ): ValidationResult {

        BlankValidator(messages).validate(login, password, repeatedPassword, email).messageOrNull()
            ?.let { message -> return Invalid(message) }

        NoDiacriticsValidator(messages).validate(login, password, email).messageOrNull()
            ?.let { message -> return Invalid(message) }

        SignInValidator(messages).validate(login, password).messageOrNull()
            ?.let { message -> return Invalid(message) }

        EqualsValidator(messages).validate(password, repeatedPassword).messageOrNull()
            ?.let { return Invalid(messages.passwordNotEqual) }

        EmailValidator(messages).validate(email).messageOrNull()
            ?.let { message -> return Invalid(message) }

        return Valid
    }
}