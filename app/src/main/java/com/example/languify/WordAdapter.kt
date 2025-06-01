
package com.example.proje
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.proje.R
import com.example.proje.Word

class WordAdapter : ListAdapter<Word, WordAdapter.WordViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Word>() {
            override fun areItemsTheSame(oldItem: Word, newItem: Word) = oldItem.WordID == newItem.WordID
            override fun areContentsTheSame(oldItem: Word, newItem: Word) = oldItem == newItem
        }
    }

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eng: TextView = itemView.findViewById(R.id.EngWordName)
        val tur: TextView = itemView.findViewById(R.id.TurWordName)
        val image: ImageView = itemView.findViewById(R.id.imageView) // doğru ID
        val WordSamples:TextView=itemView.findViewById(R.id.WordSamples)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = getItem(position)
        holder.eng.text = word.EngWordName
        holder.tur.text = word.TurWordName
        holder.WordSamples.text=word.WordSamples

        word.imagepath?.let { path ->
            val bitmap = BitmapFactory.decodeFile(path)
            if (bitmap != null) {
                holder.image.setImageBitmap(bitmap)
            } else {
                holder.image.setImageResource(R.drawable.placeholder)
            }
        }
            ?: holder.image.setImageResource(R.drawable.placeholder)
    }
}
