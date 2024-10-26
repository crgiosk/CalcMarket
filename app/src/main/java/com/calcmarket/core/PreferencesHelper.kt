package com.calcmarket.core

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferencesHelper @Inject constructor(
    @ApplicationContext val context: Context
) {

    private val preferences: SharedPreferences get() = getSecuredSharedPreferences(context)

    private fun getSecuredSharedPreferences(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(context,
            NEW_PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    }

    fun migrateOldPreferences() {
        val oldPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        if (oldPreferences.all.isNotEmpty()) {
            migratePreferences(oldPreferences, preferences)
        }

    }

    private fun migratePreferences(pOldPreferences: SharedPreferences, preferences: SharedPreferences) {
        val oldPreferences: MutableMap<String, *> = pOldPreferences.all
        for (entry in oldPreferences) {
            val key = entry.key
            when (val value: Any? = entry.value) {
                is Int -> {
                    preferences.edit().run {
                        putInt(key, value)
                        apply()
                    }
                }

                is Long -> {
                    preferences.edit().run {
                        putLong(key, value)
                        apply()
                    }
                }

                is Float -> {
                    preferences.edit().run {
                        putFloat(key, value)
                        apply()
                    }
                }

                is String -> {
                    preferences.edit().run {
                        putString(key, value)
                        apply()
                    }
                }

                is Boolean -> {
                    preferences.edit().run {
                        putBoolean(key, value)
                        apply()
                    }
                }

                else -> {
                    preferences.edit().run {
                        putString(key, value.toString())
                        apply()
                    }
                }
            }
            pOldPreferences.edit().remove(key).apply()

        }
    }

    var lastLogin: Long
        get() = preferences.getLong(LAST_LOGIN_ATTEMPT, 0L)
        set(value) {
            preferences.edit().run {
                putLong(LAST_LOGIN_ATTEMPT, value)
                apply()
            }
        }


    companion object {
        const val PREF_NAME = "preferences_key"
        const val NEW_PREF_NAME = "new_preferences_key"
        const val LAST_LOGIN_ATTEMPT = "last_time_login_key"
    }
}