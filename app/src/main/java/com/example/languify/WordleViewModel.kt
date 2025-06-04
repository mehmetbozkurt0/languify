package com.example.proje

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordleViewModel(application: Application) : AndroidViewModel(application) {
    private val db = UserDatabase.getDatabase(application)

    private val _targetWord = MutableLiveData<String?>()
    val targetWord: MutableLiveData<String?> = _targetWord

    fun loadRandomWord() {
        viewModelScope.launch(Dispatchers.IO) {
            val wordList = db.WordDao().getWordsForWordle()
            val randomWord = wordList.randomOrNull()?.EngWordName?.uppercase()
            _targetWord.postValue(randomWord)
        }
    }
}