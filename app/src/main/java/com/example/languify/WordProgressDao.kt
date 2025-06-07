package com.example.languify

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface WordProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: WordProgress)

    @Update
    suspend fun updateProgress(progress: WordProgress)


    @Query("SELECT * FROM WordProgress WHERE word_id = :wordId AND user_id = :userId")
    suspend fun getProgress(wordId: Int, userId: Int): WordProgress?

    @Delete
    suspend fun deleteProgress(progress: WordProgress)
    @Query("""
        SELECT * FROM WordProgress 
        WHERE user_id = :userId 
        AND is_learned = 0 
        AND date(next_due_date) != '' 
        AND date(next_due_date) <= date(:today)
    """)
    suspend fun getDueWords(today: String, userId: Int): List<WordProgress>


    @Query("SELECT word_id FROM WordProgress WHERE user_id = :userId AND repetition_stage >= 6")
    suspend fun getLearnedWordIds(userId: Int): List<Int>

    @Query("SELECT * FROM WordProgress WHERE user_id = :userId")
    suspend fun getAllProgressForUser(userId: Int): List<WordProgress>

    @Query("UPDATE WordProgress SET repetition_stage = 6 WHERE repetition_stage > 6")
    suspend fun normalizeStages()

    @Query("UPDATE WordProgress SET is_learned = 1 WHERE word_id % 2 = 0 AND user_id = :userId")
    suspend fun setLearnedWordsForTest(userId: Int)

    @Query("""
    SELECT Word.* FROM Word
    INNER JOIN WordProgress ON Word.WordID = WordProgress.word_id
    WHERE WordProgress.is_learned = 1 AND WordProgress.user_id = :userId
""")
    suspend fun getLearnedWordsFull(userId: Int): List<Word>
}