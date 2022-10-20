package pl.tkadziolka.snipmeandroid.domain.snippets

enum class SnippetScope {
    ALL, PUBLIC, OWNED, SHARED_FOR;
}

fun SnippetScope.value() = this.name.toLowerCase()