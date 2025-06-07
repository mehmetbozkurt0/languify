package com.example.languify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.languify.databinding.FragmentMenuBinding
import kotlinx.coroutines.launch

class menu : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPrefSettings = requireActivity().getSharedPreferences("settings_prefs", 0)
        val musicEnabled = sharedPrefSettings.getBoolean("music_enabled", true) // Varsayılan: açık

        if (musicEnabled && !SoundManager.isPlaying()) {
            SoundManager.startMusic(requireContext(), R.raw.background)
        }

        binding.button8.setOnClickListener {
            word(it)
        }

        binding.button9.setOnClickListener {
            wordle(it)
        }

        binding.button7.setOnClickListener {
            quiz(it)
        }

        binding.buttonAIStory.setOnClickListener {
            aiHikaye(it)
        }

        binding.button15.setOnClickListener{
            ayarlar(it)
        }

        binding.buttonTestData.setOnClickListener {
            val userId = requireActivity().getSharedPreferences("user_prefs", 0).getInt("userId", -1)
            lifecycleScope.launch {
                val db = UserDatabase.getDatabase(requireContext())
                db.WordProgressDao().setLearnedWordsForTest(userId)
                Toast.makeText(requireContext(), "Test verisi yüklendi!", Toast.LENGTH_SHORT).show()
            }
        }


        val sharedPref = requireActivity().getSharedPreferences("user_prefs", 0)
        val userId = sharedPref.getInt("userId", -1)

        if (userId != -1) {
            lifecycleScope.launch {
                val db = UserDatabase.getDatabase(requireContext())
                val currentUser = db.userdao().getUserById(userId)
                binding.txtWelcome.text = "Hoş geldin, ${currentUser.name}!"
            }
        }
    }

    fun word(view: View) {
        val action = menuDirections.actionMenuToWords()
        Navigation.findNavController(view).navigate(action)
    }

    fun aiHikaye(view: View) {
        val action = menuDirections.actionMenuToAIHikayeFragment()
        Navigation.findNavController(view).navigate(action)
    }

    fun wordle(view: View) {
        val action = menuDirections.actionMenuToWordle()
        Navigation.findNavController(view).navigate(action)
    }

    fun quiz(view: View) {
        val action = menuDirections.actionMenuToQuizStartFragment()
        findNavController().navigate(action)
    }
    fun ayarlar(view:View) {
        val action = menuDirections.actionMenuToAyarlar()
        Navigation.findNavController(view).navigate(action)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
