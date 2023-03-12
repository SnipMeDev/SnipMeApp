package pl.tkadziolka.snipmeandroid.infrastructure.local

import pl.tkadziolka.snipmeandroid.util.PreferencesUtil

private const val TOKEN_KEY = "7e3a40ea-32d2-4248-bcfb-297f2f41246e"

class AuthPreferences(private val prefs: PreferencesUtil) {
    fun saveToken(token: String) {
        prefs.save(TOKEN_KEY, token)
    }

    fun getToken(): String? = prefs.get(TOKEN_KEY)

    fun clearToken() {
        prefs.remove(TOKEN_KEY)
    }
}