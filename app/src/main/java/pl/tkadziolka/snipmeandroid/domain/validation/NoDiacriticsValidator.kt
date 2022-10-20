package pl.tkadziolka.snipmeandroid.domain.validation

import android.icu.text.Normalizer2
import android.os.Build
import pl.tkadziolka.snipmeandroid.domain.message.ValidationMessages
import java.text.Normalizer

class NoDiacriticsValidator(messages: ValidationMessages) : MultiValidator(messages) {
    override fun validate(vararg phrases: String): ValidationResult {
        val correct = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            phrases.all { Normalizer2.getNFDInstance().isNormalized(it) }
        } else {
            phrases.all {  Normalizer.isNormalized(it, Normalizer.Form.NFD) }
        }

        return if (correct) Valid else Invalid(messages.phraseHasDiacritics)
    }
}