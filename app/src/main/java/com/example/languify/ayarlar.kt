package com.example.proje

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.proje.databinding.FragmentAyarlarBinding

class ayarlar : Fragment() {

    private var _binding: FragmentAyarlarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAyarlarBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(başlat/durdur)
        binding.buttonStopMusic.setOnClickListener {
            SoundManager.toggleMusic(requireContext(), R.raw.background)
        }
        binding.button10.setOnClickListener {
            kvKk(it)
        }

        binding.imageView14.setOnClickListener {
            ayarlartomenu( it )
        }

        binding.button12.setOnClickListener {
            ayarlartoiletisim(it)
        }
    }
    fun kvKk(view: View){
        val action = ayarlarDirections.actionAyarlarToKvkk()
        Navigation.findNavController(view).navigate(action)
    }

    fun ayarlartomenu(view: View){
        val action = ayarlarDirections.actionAyarlarToMenu()
        Navigation.findNavController(view).navigate(action)
    }
    fun ayarlartoiletisim(view: View){
        val action = ayarlarDirections.actionAyarlarToIletisim()
        Navigation.findNavController(view).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}