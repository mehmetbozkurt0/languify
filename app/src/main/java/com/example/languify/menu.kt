package com.example.proje

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.proje.databinding.FragmentMenuBinding
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

        binding.button5.setOnClickListener {
            word(it)
        }

        binding.button9.setOnClickListener {
            wordle(it)
        }

        binding.button7.setOnClickListener {
            quiz(it)
        }

        binding.button15.setOnClickListener{
            ayarlar(it)
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
