package com.santiago.navigationcomponentexample.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.santiago.navigationcomponentexample.model.database.dao.UserDao
import com.santiago.navigationcomponentexample.model.database.entities.UserEntity


@Database(entities = [UserEntity::class], version = 1)
abstract class UsuarioDataBase: RoomDatabase() {
    abstract fun getUserDao():UserDao
}