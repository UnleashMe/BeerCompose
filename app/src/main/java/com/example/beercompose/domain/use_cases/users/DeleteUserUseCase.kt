package com.example.beercompose.domain.use_cases.users

import android.content.Context
import com.example.beercompose.R
import com.example.beercompose.data.settings.AppSettings
import com.example.beercompose.domain.model.User
import com.example.beercompose.domain.repositories.UserRepository
import com.example.beercompose.util.PasswordException
import com.example.beercompose.util.security.SecurityUtils

class DeleteUserUseCase(
    private val appSettings: AppSettings,
    private val userRepository: UserRepository,
    private val securityUtils: SecurityUtils,
    private val context: Context
) {
    suspend operator fun invoke(password: CharArray) {
        val user = appSettings.getCurrentUser()
        val userEntity = userRepository.findUserByNumber(user.phoneNumber)!!
        val saltBytes = securityUtils.stringToBytes(userEntity.salt)
        val hashBytes = securityUtils.passwordToHash(password, saltBytes)
        password.fill('*')
        val hashString = securityUtils.bytesToString(hashBytes)

        if (userEntity.hash != hashString) {
            throw PasswordException.OldPasswordException(context.getString(R.string.wrong_password_exception))
        }
        userRepository.deleteUser(user.phoneNumber)
        appSettings.setCurrentUser(User())
    }
}