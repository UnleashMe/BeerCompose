package com.example.beercompose.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.beercompose.data.database.entities.MenuItemEntity
import com.example.beercompose.data.database.entities.UserEntity
import com.example.beercompose.data.database.entities.UserMenuItemCart
import com.example.beercompose.data.database.entities.UserMenuItemLikes

@Database(
    entities = [
        MenuItemEntity::class,
        UserEntity::class,
        UserMenuItemLikes::class,
        UserMenuItemCart::class
    ],
    version = 1
)
abstract class BeersDatabase : RoomDatabase() {

    abstract fun getBeersDao(): BeersDao

    abstract fun getUserDao(): UserDao

    abstract fun getCartDao(): CartDao

    abstract fun getLikesDao(): LikesDao
}