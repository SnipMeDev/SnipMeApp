package pl.tkadziolka.snipmeandroid.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

private const val PREFS_FILENAME = "5ee122cc-6fc4-11eb-9439-0242ac130002"

@Suppress("UNCHECKED_CAST")
class PreferencesUtil(context: Context) {

    private val pref: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)

    fun <T> save(key: String, value: T) {
        val editor = pref.edit()
        when(value) {
            is Boolean -> editor.putBoolean(key, value)
            is Int -> editor.putInt(key, value)
            is String -> editor.putString(key, value)
            else -> Unit
        }
        editor.apply()
    }

    fun <T> get(key: String): T? = try {
        pref.all[key] as T?
    } catch (e: Exception) {
        null
    }

    fun remove(key: String) {
        pref.edit().remove(key).apply()
    }

    fun clear() {
        pref.edit().clear().apply()
    }

}