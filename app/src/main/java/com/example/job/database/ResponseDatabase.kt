package com.example.job.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ResponseDataEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class ResponseDatabase : RoomDatabase() {

    abstract fun responseDataDao(): ResponseDataDao

    companion object {
        @Volatile
        private var INSTANCE: ResponseDatabase? = null

        fun getDatabase(context: Context): ResponseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ResponseDatabase::class.java,
                    "my-database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}