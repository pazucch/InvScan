package com.example.exibirpatrimoniosactivity.data

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase


@Database(entities = [Patrimonio::class, Local::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun patrimonioDAO(): PatrimonioDAO

    abstract fun localDAO(): LocalDAO

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            val tempInstance = INSTANCE

            if (tempInstance != null) {
                // o banco existe
                return tempInstance
            } else {
                // criar o banco
                synchronized(this) {

                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    ).build()

                    INSTANCE = instance
                    return instance

                }
            }
        }
    }
}
