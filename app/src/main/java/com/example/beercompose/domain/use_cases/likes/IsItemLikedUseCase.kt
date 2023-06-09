package com.example.beercompose.domain.use_cases.likes

import com.example.beercompose.data.database.entities.UserMenuItemLikes
import com.example.beercompose.data.settings.AppSettings
import com.example.beercompose.domain.repositories.LikesRepository

class IsItemLikedUseCase(
    private val appSettings: AppSettings,
    private val userRepository: LikesRepository
) {
    suspend operator fun invoke(id: String): UserMenuItemLikes? {
        val userNumber = appSettings.getCurrentUser().phoneNumber
        return userRepository.findLike(userNumber, id)
    }
}