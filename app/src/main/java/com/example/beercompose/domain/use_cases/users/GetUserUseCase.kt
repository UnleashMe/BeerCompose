package com.example.beercompose.domain.use_cases.users

import com.example.beercompose.data.settings.AppSettings
import com.example.beercompose.domain.model.User

class GetUserUseCase(
    private val appSettings: AppSettings
) {
    suspend operator fun invoke(): User {
        return appSettings.getCurrentUser()
    }
}