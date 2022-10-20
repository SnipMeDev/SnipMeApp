package pl.tkadziolka.snipmeandroid.domain.snippets

import pl.tkadziolka.snipmeandroid.domain.snippets.SnippetLanguageType.*

object SnippetLanguageMapper {

    fun toString(language: SnippetLanguageType): String =
        languageMap
            .filterValues { it == language }
            .keys
            .firstOrNull()
            .orEmpty()

    fun fromString(language: String?): SnippetLanguageType =
        if (language.isNullOrEmpty()) {
            UNKNOWN
        } else {
            languageMap[language] ?: UNKNOWN
        }

    val languageMap = mapOf(
        "C" to C,
        "C++" to CPP,
        "Objective-C" to OBJECTIVE_C,
        "C#" to C_SHARP,
        "Java" to JAVA,
        "Kotlin" to KOTLIN,
        "Bash" to BASH,
        "Python" to PYTHON,
        "Perl" to PERL,
        "Ruby" to RUBY,
        "Swift (Apple programming language)" to SWIFT,
        "JavaScript" to JAVASCRIPT,
        "CoffeeScript" to COFFEESCRIPT,
        "Rust" to RUST,
        "BASIC" to BASIC,
        "Clojure" to CLOJURE,
        "CSS" to CSS,
        "Dart" to DART,
        "Erlang" to ERLANG,
        "Go" to GO,
        "Haskell" to HASKELL,
        "Lisp" to LISP,
        "LLVM" to LLVM,
        "Lua" to LUA,
        "MATLAB" to MATLAB,
        "ML" to ML,
        "MUMPS" to MUMPS,
        "Nemerle" to NEMERLE,
        "Pascal" to PASCAL,
        "R" to R,
        "RD" to RD,
        "Scala" to SCALA,
        "SQL" to SQL,
        "TeX" to TEX,
        "Visual Basic" to VB,
        "VHDL" to VHDL,
        "Tcl" to TCL,
        "XQuery" to XQUERY,
        "YAML" to YAML,
        "Markdown" to MARKDOWN,
        "JSON" to JSON,
        "XML" to XML,
        "Proto" to PROTO,
        "Regex" to REGEX
    )
}

