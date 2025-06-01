package com.example.proje

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.proje.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class login : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            giris(it)
        }

        binding.button2.setOnClickListener {
            kayıt(it)
        }

        binding.textView7.setOnClickListener {
            rest(it)
        }
    }

    fun giris(view: View) {
        val name = binding.editTextText.text.toString().trim()
        val password = binding.editTextTextPassword.text.toString().trim()

        if (name.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val db = UserDatabase.getDatabase(requireContext())
            val user = db.userdao().login(name, password)

            if (user != null) {
                Toast.makeText(requireContext(), "Başarılı Giriş", Toast.LENGTH_LONG).show()
                val sharedPref = requireActivity().getSharedPreferences("user_prefs", 0)
                sharedPref.edit().putInt("userId", user.id).apply()

                val action = loginDirections.actionLoginToMenu()
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Başarısız", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun kayıt(view: View) {
        val name = binding.editTextText.text.toString().trim()
        val password = binding.editTextTextPassword.text.toString().trim()

        if (name.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            return
        }

        val newUser = user(name = name, password = password)

        lifecycleScope.launch {
            val db = UserDatabase.getDatabase(requireContext())
            val insertedId = db.userdao().insertUser(newUser)

            if (insertedId > 0) {
                val sharedPref = requireActivity().getSharedPreferences("user_prefs", 0)
                sharedPref.edit().putInt("userId", insertedId.toInt()).apply()

                Toast.makeText(requireContext(), "Kayıt başarılı", Toast.LENGTH_SHORT).show()

                val action = loginDirections.actionLoginToMenu()
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Kayıt başarısız", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun rest(view: View) {
        val action = loginDirections.actionLoginToRest()
        Navigation.findNavController(view).navigate(action)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
