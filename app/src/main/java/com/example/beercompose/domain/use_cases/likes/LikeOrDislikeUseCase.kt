package com.example.beercompose.domain.use_cases.likes

import com.example.beercompose.data.database.entities.UserMenuItemLikes
import com.example.beercompose.data.settings.AppSettings
import com.example.beercompose.domain.repositories.LikesRepository

class LikeOrDislikeUseCase(
    private val appSettings: AppSettings,
    private val userRepository: LikesRepository
) {
    suspend operator fun invoke(domainItemId: String, likedItems: List<String>): List<String> {
        val user = appSettings.getCurrentUser()
        if (user.name != "") {
            val likedItemsIds =
                userRepository.getUserLikes(user.phoneNumber).menuItems.map { it.UID }
            return if (likedItemsIds.contains(domainItemId)) {
                userRepository.removeLike(user.phoneNumber, domainItemId)
                likedItems.filterNot { it == domainItemId }
            } else {
                userRepository.addLike(UserMenuItemLikes(user.phoneNumber, domainItemId))
                likedItems + domainItemId
            }
        }
        return emptyList()
    }
}