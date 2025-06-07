package com.example.languify

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.languify.databinding.FragmentWordleBinding

class wordle : Fragment() {

    private var _binding: FragmentWordleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WordleViewModel by viewModels()
    private val maxRows = 6
    private var currentRow = 0
    private val cellViews = mutableListOf<TextView>()
    private var targetWord = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentWordleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGrid()

        viewModel.targetWord.observe(viewLifecycleOwner) { word ->
            if (!word.isNullOrEmpty()) {
                targetWord = word
                Toast.makeText(requireContext(), "Kelime yüklendi", Toast.LENGTH_SHORT).show()
            }
        }

        binding.submitGuessButton.setOnClickListener {
            val guess = binding.guessInput.text.toString().trim().uppercase()
            if (guess.length != 5) {
                Toast.makeText(requireContext(), "Lütfen 5 harfli bir kelime girin", Toast.LENGTH_SHORT).show()
            } else {
                checkGuess(guess)
            }
        }

        binding.guessInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.submitGuessButton.performClick()
                true
            } else {
                false
            }
        }

        viewModel.loadRandomWord()
    }

    private fun setupGrid() {
        for (i in 0 until maxRows * 5) {
            val cell = TextView(requireContext()).apply {
                textSize = 24f
                setBackgroundResource(android.R.drawable.alert_light_frame)
                gravity = android.view.Gravity.CENTER
                setPadding(8, 8, 8, 8)
                setTextColor(Color.BLACK)
                layoutParams = ViewGroup.LayoutParams(120, 120)
            }
            binding.wordGrid.addView(cell)
            cellViews.add(cell)
        }
    }

    private fun checkGuess(guess: String) {
        val start = currentRow * 5

        for (i in 0 until 5) {
            val cell = cellViews[start + i]
            val ch = guess[i]
            when {
                ch == targetWord[i] -> cell.setBackgroundColor(Color.GREEN)
                ch in targetWord -> cell.setBackgroundColor(Color.YELLOW)
                else -> cell.setBackgroundColor(Color.DKGRAY)
            }
            cell.text = ch.toString()
        }

        if (guess == targetWord) {
            Toast.makeText(requireContext(), "Tebrikler, doğru kelime!", Toast.LENGTH_LONG).show()
            binding.guessInput.isEnabled = false
            binding.submitGuessButton.isEnabled = false
        } else {
            currentRow++
            if (currentRow >= maxRows) {
                Toast.makeText(requireContext(), "Bilemedin! Doğru kelime: $targetWord", Toast.LENGTH_LONG).show()
                binding.guessInput.isEnabled = false
                binding.submitGuessButton.isEnabled = false
            }
        }

        binding.guessInput.text.clear()
    }

    override fun onDestroyView() {
        //  Müzik durdurulmaz
        super.onDestroyView()
        _binding = null
    }
}