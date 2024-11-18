package com.santiago.navigationcomponentexample.model.database.providers

import android.content.Context
import androidx.room.Room
import com.santiago.navigationcomponentexample.model.database.UsuarioDataBase

object UsuarioDatabaseProvider {
    fun getDatabase(context: Context): UsuarioDataBase {
        return Room.databaseBuilder(
            context,
            UsuarioDataBase::class.java,
            "app_database"
        ).build()
    }
}