package com.example.proje

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface userdao {
    @Query("SELECT * FROM user WHERE name = :name AND password = :password")
    suspend   fun login(name: String, password: String): user?
    @Insert
    suspend fun insertUser(user: user):Long
    @Query("SELECT *FROM user WHERE name=:name LIMIT 1")
    suspend fun find(name: String):user?
    @Update
    suspend fun update(user: user)
    @Query("SELECT * FROM user WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): user

}