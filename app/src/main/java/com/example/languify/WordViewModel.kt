package com.example.languify

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class WordViewModel  (application: Application) : AndroidViewModel(application) {

    private val dao = UserDatabase.getDatabase(application).WordDao()

    private val _wordList = MutableLiveData<List<Word>>()
    val wordList: LiveData<List<Word>> get() = _wordList

    fun refreshWords(context: Context) {
        viewModelScope.launch {
            val db = UserDatabase.getDatabase(context)
            val sharedPref = context.getSharedPreferences("user_prefs", 0)
            val userId = sharedPref.getInt("userId", -1)

            if (userId != -1) {
                val words = db.WordDao().getWordsByUserId(userId)
                _wordList.value = words
            }
        }
    }

}
