package dev.snipme.snipmeapp.domain.message

interface ValidationMessages {

    val passwordWrong: String
    val passwordTooShort: String
    val passwordNoSpecialChar: String
    val passwordNoUpperLower: String
    val passwordNoDigit: String
    val passwordNotEqual: String

    val emailTooShort: String
    val emailWrongStructure: String

    val phraseTooShort: String
    val phraseTooLong: String
    val phraseNoUpperLower: String
    val phraseHasDiacritics: String

    val phrasesBlank: String
    val phrasesNotEqual: String
}