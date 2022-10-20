package pl.tkadziolka.snipmeandroid.domain.message

import android.content.Context
import pl.tkadziolka.snipmeandroid.R

class RealValidationMessages(context: Context): ValidationMessages {
    private val res = context.resources

    override val passwordWrong = res.getString(R.string.validation_password_wrong)
    override val passwordTooShort = res.getString(R.string.validation_password_too_short)
    override val passwordNoSpecialChar = res.getString(R.string.validation_password_special_char)
    override val passwordNoDigit = res.getString(R.string.validation_password_digit)
    override val passwordNoUpperLower = res.getString(R.string.validation_password_lower_upper)
    override val passwordNotEqual: String = res.getString(R.string.validation_password_not_equal)

    override val emailTooShort: String = res.getString(R.string.validation_email_too_short)
    override val emailWrongStructure: String = res.getString(R.string.validation_email_wrong_structure)

    override val phraseTooShort: String = res.getString(R.string.validation_phrase_too_short)
    override val phraseTooLong: String = res.getString(R.string.validation_phrase_too_long)
    override val phraseNoUpperLower: String = res.getString(R.string.validation_phrase_lower_upper)
    override val phraseHasDiacritics: String = res.getString(R.string.validation_phrase_no_ascii)

    override val phrasesBlank = res.getString(R.string.validation_phrases_blank)
    override val phrasesNotEqual: String = res.getString(R.string.validation_phrase_too_short)
}