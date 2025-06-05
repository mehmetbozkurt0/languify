package com.example.proje

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val db = UserDatabase.getDatabase(application)

    fun getTodaysQuiz(userId: Int, limit: Int = 10): LiveData<List<Word>> = liveData {
        val today = LocalDate.now().toString()
        val dueProgress = db.WordProgressDao().getDueWords(today, userId)
        val dueWordIds = dueProgress.map { it.word_id }
        val words = db.WordDao().getWordsByIds(dueWordIds)
        emit(words)
    }

    val repeatIntervals = listOf(1L, 3L, 7L, 15L, 30L, 60L)

    suspend fun handleAnswer(wordId: Int, userId: Int, isCorrect: Boolean) {
        val today = LocalDate.now()
        val dao = db.WordProgressDao()
        val progress = dao.getProgress(wordId, userId)

        if (progress != null) {
            if (isCorrect) {
                val nextStage = progress.repetition_stage + 1
                val isLearned = if (nextStage >= 6) 1 else 0
                val nextDue = if (nextStage < 6) {
                    today.plusDays(repeatIntervals[nextStage]).toString()
                } else {
                    ""
                }
                val updated = progress.copy(
                    repetition_stage = nextStage,
                    is_learned = isLearned,
                    next_due_date = nextDue,
                    last_answered_date = today.toString()
                )
                dao.updateProgress(updated)
            } else {
                val reset = progress.copy(
                    repetition_stage = 0,
                    is_learned = 0,
                    next_due_date = today.plusDays(1).toString(),
                    last_answered_date = today.toString()
                )
                dao.updateProgress(reset)
            }
        } else {
            val nextDue = if (isCorrect)
                today.plusDays(repeatIntervals[1]).toString()
            else
                today.plusDays(1).toString()

            val newProgress = WordProgress(
                word_id = wordId,
                user_id = userId,
                repetition_stage = if (isCorrect) 1 else 0,
                is_learned = 0,
                next_due_date = nextDue,
                last_answered_date = today.toString()
            )
            dao.insertProgress(newProgress)
        }
    }
}
