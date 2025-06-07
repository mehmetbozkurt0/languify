package com.example.languify

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "WordProgress",
    foreignKeys = [
        ForeignKey(
            entity = Word::class,
            parentColumns = ["WordID"],
            childColumns = ["word_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = user::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WordProgress(@PrimaryKey(autoGenerate = true) val id: Int = 0,
                        val word_id: Int,
                        val user_id: Int,
                        val repetition_stage: Int = 0,
                        val next_due_date: String,
                        val last_answered_date: String,
                        val is_learned: Int = 0)
