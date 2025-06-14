package com.example.languify


import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch

class QuizFragment : Fragment() {

    private val quizViewModel: QuizViewModel by viewModels()
    private var wordList: List<Word> = listOf()
    private var currentIndex = 0
    private var userId = -1
    private var questionCount = 0
    private var correctCount = 0

    private lateinit var txtWord: TextView
    private lateinit var edtAnswer: EditText
    private lateinit var btnCheck: Button
    private lateinit var txtFeedback: TextView
    private lateinit var imgView: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtWord = view.findViewById(R.id.txtEnglishWord)
        edtAnswer = view.findViewById(R.id.editTurkishAnswer)
        btnCheck = view.findViewById(R.id.btnCheckAnswer)
        txtFeedback = view.findViewById(R.id.txtFeedback)
        imgView = view.findViewById(R.id.quizImage)


        questionCount = arguments?.getInt("questionCount") ?: 5
        userId = requireActivity()
            .getSharedPreferences("user_prefs", 0)
            .getInt("userId", -1)

        quizViewModel.getTodaysQuiz(userId, questionCount).observe(viewLifecycleOwner) {
            wordList = it
            if (wordList.isNotEmpty()) {
                showNextWord()
            } else {
                txtWord.text = "Bugünlük çözülmesi gereken kelime yok."
                btnCheck.isEnabled = false
            }
        }
        btnCheck.setOnClickListener {
            checkAnswer()
        }
    }
    private fun showNextWord() {
        if (wordList.isEmpty()) {
            AlertDialog.Builder(requireContext())
                .setTitle("Tebrikler!")
                .setMessage("Quiz tamamlandı!\nDoğru sayısı: $correctCount")
                .setPositiveButton("Tamam") { dialog, _ ->
                    dialog.dismiss()
                    findNavController().popBackStack()
                }
                .setCancelable(false)
                .show()
            txtWord.text = ""
            btnCheck.isEnabled = false
            edtAnswer.visibility = View.GONE
            txtFeedback.text = ""
            return
        }
        val currentWord = wordList[currentIndex]
        txtWord.text = currentWord.EngWordName
        edtAnswer.setText("")
        txtFeedback.text = ""
        val imagePath = currentWord.imagepath
        if (!imagePath.isNullOrEmpty()) {
            val scaledBitmap = getScaledBitmap(imagePath)
            if (scaledBitmap != null) {
                imgView.setImageBitmap(scaledBitmap as Bitmap?)
            } else {
                imgView.setImageResource(R.drawable.placeholder)
            }
        } else {
            imgView.setImageResource(R.drawable.placeholder)
        }
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


    private fun checkAnswer() {
        val userAnswer = edtAnswer.text.toString().trim()
        val currentWord = wordList[currentIndex]
        val correctAnswer = currentWord.TurWordName.trim()

        val isCorrect = userAnswer.equals(correctAnswer, ignoreCase = true)

        lifecycleScope.launch {
            quizViewModel.handleAnswer(currentWord.WordID, userId, isCorrect)
        }

        if (isCorrect) {
            correctCount++
            txtFeedback.text = "✔ Doğru!"

            wordList = wordList.filter { it.WordID != currentWord.WordID }
        } else {
            txtFeedback.text = "✖ Yanlış. Doğru cevap: $correctAnswer"
            currentIndex++ // yanlış bildiği için ilerle
        }

        // Tekrar aynı kelime gelmesin, currentIndex sınır kontrolü
        if (currentIndex >= wordList.size) {
            currentIndex = 0
        }

        Handler(Looper.getMainLooper()).postDelayed({
            showNextWord()
        }, 1500)
    }


}
