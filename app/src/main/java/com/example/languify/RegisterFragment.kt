package com.example.languify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.languify.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            registerUser()
        }

        binding.btnBackToLogin.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLogin()
            findNavController().navigate(action)
        }
    }

    private fun registerUser() {
        val name = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (name.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            return
        }

        val newUser = user(name = name, password = password)

        lifecycleScope.launch {
            val db = UserDatabase.getDatabase(requireContext())
            val insertedId = db.userdao().insertUser(newUser)

            if (insertedId > 0) {
                Toast.makeText(requireContext(), "Kayıt başarılı", Toast.LENGTH_SHORT).show()
                val action = RegisterFragmentDirections.actionRegisterFragmentToLogin()
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Kayıt başarısız", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
