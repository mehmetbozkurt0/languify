package com.example.proje

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.proje.databinding.FragmentMenuBinding
import com.example.proje.databinding.FragmentQuizStartBinding
import java.time.LocalDate

class QuizStartFragment : Fragment() {
    private var _binding: FragmentQuizStartBinding? = null
    private val binding get() = _binding!!

    private lateinit var edtCount: EditText
    private lateinit var btnStart: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View? {
        _binding = FragmentQuizStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val edtCount = view.findViewById<EditText>(R.id.editQuestionCount)
        val btnStart = view.findViewById<Button>(R.id.btnStartQuiz)

        btnStart.setOnClickListener {
            val sharedPref = requireActivity().getSharedPreferences("user_prefs", 0)
            val today = LocalDate.now().toString()
            val userId = sharedPref.getInt("userId", -1)
            val lastQuizDate = sharedPref.getString("lastQuizDate_$userId", "")

            // GÜNLÜK TEKRAR KISITLAMASI DEVRE DIŞI BIRAKILDI
            // if (today == lastQuizDate) {
            //     Toast.makeText(requireContext(), "Bugün quiz çözdünüz.", Toast.LENGTH_LONG).show()
            //     return
            // }

            val count = edtCount.text.toString().toIntOrNull() ?: 5
            val action = QuizStartFragmentDirections.actionQuizStartFragmentToQuizFragment(count)
            findNavController().navigate(action)
        }

        binding.imageView3.setOnClickListener {
            menuQuiz(it)
        }

        binding.imageView4.setOnClickListener {
            ayarQuiz(it)
        }
    }

    fun menuQuiz(view: View){
        val action = QuizStartFragmentDirections.actionQuizStartFragmentToMenu()
        Navigation.findNavController(view).navigate(action)
    }

    fun ayarQuiz(view: View){
        val action = QuizStartFragmentDirections.actionQuizStartFragmentToAyarlar()
        Navigation.findNavController(view).navigate(action)
    }

}
