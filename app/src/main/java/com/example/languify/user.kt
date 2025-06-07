package com.example.languify

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "user")
data class user (
   @PrimaryKey(autoGenerate = true) val id:Int=0,
   @NonNull val name:String,
   @NonNull var password:String
)
