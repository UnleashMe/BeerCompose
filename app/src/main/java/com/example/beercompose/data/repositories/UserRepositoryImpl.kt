package com.example.beercompose.data.repositories

import com.example.beercompose.data.database.UserDao
import com.example.beercompose.data.database.entities.UserEntity
import com.example.beercompose.domain.repositories.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun addUser(user: UserEntity) = userDao.addUser(user)

    override suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)

    override suspend fun deleteUser(number: String) = userDao.deleteUser(number)

    override suspend fun changeUserName(number: String, name: String) =
        userDao.changeUserName(number, name)

    override suspend fun changeUserPassword(number: String, hash: String, salt: String) =
        userDao.changeUserPassword(number, hash, salt)

    override suspend fun findUserByNumber(number: String): UserEntity? =
        userDao.findUserByNumber(number)
}
