package com.example.beercompose.domain.use_cases.users

import android.content.Context
import com.example.beercompose.R
import com.example.beercompose.domain.model.User
import com.example.beercompose.domain.repositories.UserRepository
import com.example.beercompose.util.PasswordException
import com.example.beercompose.util.UserDoesntExistException
import com.example.beercompose.util.security.SecurityUtils

class FindUserByNumberUseCase(
    private val repository: UserRepository,
    private val securityUtils: SecurityUtils,
    private val context: Context
) {
    suspend operator fun invoke(number: String, password: CharArray): User {
        val user = repository.findUserByNumber(number)
            ?: throw UserDoesntExistException(context.getString(R.string.user_doesnt_exist_exception))
        val saltBytes = securityUtils.stringToBytes(user.salt)
        val hashBytes = securityUtils.passwordToHash(password, saltBytes)
        val hashString = securityUtils.bytesToString(hashBytes)
        password.fill('*')
        if (user.hash != hashString) {
            throw PasswordException.OldPasswordException(context.getString(R.string.wrong_password_exception))
        }
        return user.toUser()
    }
}