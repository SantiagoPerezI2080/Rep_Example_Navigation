package com.santiago.navigationcomponentexample.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.santiago.navigationcomponentexample.model.database.entities.UserEntity


@Dao
interface UserDao {
    @Query("SELECT * FROM usuarios ORDER BY nombre DESC")
    suspend fun getAllUsers(): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: UserEntity)

    @Query("DELETE FROM usuarios WHERE id = :id")
    suspend fun eliminarPorId(id: Int)
}

