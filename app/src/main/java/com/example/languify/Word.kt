package com.example.languify

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Word",
    foreignKeys = [ForeignKey(
        entity = user::class,
        parentColumns = ["id"],
        childColumns = ["id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Word(
    @PrimaryKey(autoGenerate = true) val WordID: Int = 0,
    @NonNull val EngWordName: String,
    @NonNull val TurWordName: String,
    val imagepath: String?,
    val id: Int, // user tablosuyla ilişkilendirmek için eklendi
    @NonNull val WordSamples:String
)
