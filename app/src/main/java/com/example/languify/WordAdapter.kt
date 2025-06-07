
package com.example.languify
import android.graphics.Bitmap
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
        holder.WordSamples.text = word.WordSamples

        word.imagepath?.let { path ->
            val scaledBitmap = getScaledBitmap(path)
            if (scaledBitmap != null) {
                holder.image.setImageBitmap(scaledBitmap)
            } else {
                holder.image.setImageResource(R.drawable.placeholder)
            }
        } ?: holder.image.setImageResource(R.drawable.placeholder)
    }

    fun getScaledBitmap(path: String, targetSize: Int = 1024): Bitmap? {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(path, options)

        options.inSampleSize = calculateInSampleSize(options, targetSize, targetSize)
        options.inJustDecodeBounds = false

        return BitmapFactory.decodeFile(path, options)
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}
