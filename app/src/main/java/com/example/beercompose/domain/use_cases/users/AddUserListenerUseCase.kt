package com.example.beercompose.domain.use_cases.users

import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.beercompose.data.settings.AppSettings
import com.example.beercompose.util.Constants.USER_PREFERENCES_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class AddUserListenerUseCase(
    private val appSettings: AppSettings
) {
    operator fun invoke(): Flow<String> {
        val userKey = stringPreferencesKey(USER_PREFERENCES_KEY)

        return appSettings.addListener().map {
            it[userKey] ?: ""
        }.distinctUntilChanged()
    }
}