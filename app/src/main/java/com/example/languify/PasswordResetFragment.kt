package com.example.languify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.languify.databinding.FragmentRestBinding
import kotlinx.coroutines.launch


class PasswordResetFragment : Fragment() {
    private var _binding: FragmentRestBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var temp=binding.editTextText6.text.toString().trim()
        binding.button15.setOnClickListener {
            sifre(it)
        }
    }

    fun sifre(view: View){
        var temp=binding.editTextText6.text.toString().trim()
        var sıfretemp=binding.editTextTextPassword2.text.toString().trim()

        lifecycleScope.launch {
            val db=UserDatabase.getDatabase(requireContext())
            var user=db.userdao().find(temp)
            if (user==null){
                Toast.makeText(requireContext(),"yanlıs",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(),"dogru",Toast.LENGTH_SHORT).show()
                user.password=sıfretemp
                db.userdao().update(user)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}





