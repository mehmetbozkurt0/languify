package com.example.languify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuizAdapter(
    private val onAnswerSelected: (Word, Boolean) -> Unit) : RecyclerView.Adapter<QuizAdapter.WordViewHolder>() {

    private val words = mutableListOf<Word>()

    fun submitList(newList: List<Word>) {
        words.clear()
        words.addAll(newList)
        notifyDataSetChanged()
    }
    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(word: Word) {
            itemView.findViewById<TextView>(R.id.txtWord).text = word.EngWordName

            itemView.findViewById<Button>(R.id.btnCorrect).setOnClickListener {
                onAnswerSelected(word, true)
            }
            itemView.findViewById<Button>(R.id.btnWrong).setOnClickListener {
                onAnswerSelected(word, false)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quiz_word, parent, false)
        return WordViewHolder(view)
    }
    override fun getItemCount() = words.size

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(words[position])
    }
}