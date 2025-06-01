package com.example.proje

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [user::class,Word::class,WordProgress::class], version = 6, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userdao(): userdao
    abstract fun WordDao():WordDao
    abstract fun WordProgressDao():WordProgressDao

    companion object {
        @Volatile private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "UserWord.db"
                )
                    .fallbackToDestructiveMigration() // Bu satır schema uyumsuzluğu varsa tüm veritabanını sıfırlar
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
