package dev.snipme.snipmeapp.domain.snippets

import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository

class SetupDemoSnippetsUseCase(
    private val snippetRepository: SnippetRepository
) {

    operator fun invoke() =
        snippetRepository.create(
            title = "Your first snippet",
            code = KOTLIN_SAMPLE,
            language = SnippetLanguageType.KOTLIN.name,
            visibility = SnippetVisibility.PUBLIC,
            userId = 1,
            favorite = false
        )

//        snippetRepository.create(
//            title = "Hello World",
//            code = "console.log('Hello, World!')",
//            language = "JavaScript",
//            visibility = SnippetVisibility.PUBLIC,
//            userId = 1,
//            favorite = false
//        )

//        snippetRepository.create(
//            title = "Hello World",
//            code = "print('Hello, World!')",
//            language = "Python",
//            visibility = SnippetVisibility.PUBLIC,
//            userId = 1,
//            favorite = false
//        )

//        snippetRepository.create(
//            title = "Hello World",
//            code = "System.out.println(\"Hello, World!\");",
//            language = "Java",
//            visibility = SnippetVisibility.PUBLIC,
//            userId = 1,
//            favorite = false
//        )
    }
//}

val KOTLIN_SAMPLE = """
// Data class
data class User(val id: Int, val name: String, val email: String)

// Extension function
fun String.isValidEmail(): Boolean {
    return this.contains("@") && this.contains(".")
}

// Higher-order function
fun <T> List<T>.customFilter(predicate: (T) -> Boolean): List<T> {
    val result = mutableListOf<T>()
    for (item in this) {
        if (predicate(item)) {
            result.add(item)
        }
    }
    return result
}

// Sealed class
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

// Coroutines
import kotlinx.coroutines.*

fun main() = runBlocking {
    val users = listOf(
        User(1, "Alice", "alice@example.com"),
        User(2, "Bob", "bobexample.com"), // Invalid email
        User(3, "Charlie", "charlie@example.com")
    )

    // Using extension function
    users.forEach { user ->
        println("${'$'}{user.name}'s email is valid: ${'$'}{user.email.isValidEmail()}")
    }

    // Using higher-order function
    val validUsers = users.customFilter { it.email.isValidEmail() }
    println("Valid users: ${'$'}validUsers")

    // Using coroutines
    val result = fetchUserData()
    when (result) {
        is Result.Success -> println("Fetched user data: ${'$'}{result.data}")
        is Result.Error -> println("Error fetching user data: ${'$'}{result.exception.message}")
    }
}

// Simulate a network call using coroutines
suspend fun fetchUserData(): Result<List<User>> {
    return withContext(Dispatchers.IO) {
        delay(1000) // Simulate network delay
        try {
            val data = listOf(
                User(4, "Dave", "dave@example.com"),
                User(5, "Eve", "eve@example.com")
            )
            Result.Success(data)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
"""