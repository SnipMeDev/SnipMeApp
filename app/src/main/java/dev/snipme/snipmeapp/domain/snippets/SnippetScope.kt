package dev.snipme.snipmeapp.domain.snippets

enum class SnippetScope(val visibleName: String) {
    ALL("All"), VISIBLE("Visible"), HIDDEN("Hidden");
}

fun SnippetScope.value() = this.name.toLowerCase()