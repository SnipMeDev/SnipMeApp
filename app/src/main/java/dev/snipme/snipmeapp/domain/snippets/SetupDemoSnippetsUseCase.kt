package dev.snipme.snipmeapp.domain.snippets

import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository
import io.reactivex.Completable
import io.reactivex.Single

class SetupDemoSnippetsUseCase(
    private val snippetRepository: SnippetRepository
) {

    operator fun invoke(): Completable {
        val demoSetup = snippetRepository.getDemoSetupStatus()

        return if (!demoSetup) {
            setupDemoSnippets().andThen(
                snippetRepository.setDemoSetupStatus(true)
            )
        } else {
            Completable.complete()
        }
    }

    private fun setupDemoSnippets() = Completable.fromPublisher(
        Single.merge(
            snippetRepository.create(
                title = "Your first snippet",
                code = KOTLIN_SAMPLE,
                language = SnippetLanguageType.KOTLIN.name,
                visibility = SnippetVisibility.VISIBLE,
                favorite = false
            ),
            snippetRepository.create(
                title = "Your favorite code",
                code = JAVASCRIPT_SAMPLE,
                language = SnippetLanguageType.JAVASCRIPT.name,
                visibility = SnippetVisibility.VISIBLE,
                favorite = true
            ),
            snippetRepository.create(
                title = "Hidden one",
                code = PYTHON_SAMPLE,
                language = SnippetLanguageType.PYTHON.name,
                visibility = SnippetVisibility.HIDDEN,
                favorite = false
            ),
            snippetRepository.create(
                title = "Popular Java code",
                code = JAVA_SAMPLE,
                language = SnippetLanguageType.JAVA.name,
                visibility = SnippetVisibility.VISIBLE,
                favorite = false
            )
        ),
    )
}

private const val KOTLIN_SAMPLE = """
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

const val JAVASCRIPT_SAMPLE = """
// Async function with Promise
async function fetchUserData() {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      try {
        const data = [
          new User(1, 'Alice', 'alice@example.com'),
          new User(2, 'Bob', 'bobexample.com'), // Invalid email
          new User(3, 'Charlie', 'charlie@example.com')
        ];
        resolve(data);
      } catch (error) {
        reject(error);
      }
    }, 1000); // Simulate network delay
  });
}
"""

const val PYTHON_SAMPLE = """
# Class definition
class User:
    def __init__(self, id, name, email):
        self.id = id
        self.name = name
        self.email = email

    def __str__(self):
        return f'User(id={self.id}, name={self.name}, email={self.email})'
            
    # Function with list comprehension
    def is_valid_email(email):
        return '@' in email and '.' in email
        
    # Function with generator expression
    
    def custom_filter(predicate, items):
        return [item for item in items if predicate(item)]
"""

const val JAVA_SAMPLE = """
// Class definition
public class User {
    private final int id;
    private final String name;
    private final String email;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("User(id=%d, name=%s, email=%s)", id, name, email);
    }

    // Function with lambda expression
    public static boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    // Function with method reference
    public static List<User> customFilter(Predicate<User> predicate, List<User> items) {
        return items.stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }
}
"""