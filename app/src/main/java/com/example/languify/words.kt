package com.example.languify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.languify.databinding.FragmentWordBinding


class words : Fragment() {
    private var _binding: FragmentWordBinding? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button3.setOnClickListener {
            ekle(it)
        }
        binding.button4.setOnClickListener {
            cıkar(it)
        }
        binding.button5.setOnClickListener {
            listele(it)
        }
        binding.button12.setOnClickListener {
          ogr(it)
        }

        binding.imageView11.setOnClickListener{
            wordtomenu(it)
        }

        binding.imageView12.setOnClickListener{
            wordtoayarlar(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    fun ekle(view: View){
        val action=wordsDirections.actionWordsToWordadd()
        Navigation.findNavController(view).navigate(action)

    }
    fun cıkar(view: View){
        val action=wordsDirections.actionWordsToDelete()
        Navigation.findNavController(view).navigate(action)
    }
    fun listele(view: View){
        val action=wordsDirections.actionWordsToWordListFragment2()
        Navigation.findNavController(view).navigate(action)
    }
    fun ogr(view: View){
        val action=wordsDirections.actionWordsToLearnedWordsReportFragment()
        Navigation.findNavController(view).navigate(action)
    }

    fun wordtomenu(view: View){
        val action = wordsDirections.actionWordsToMenu()
        Navigation.findNavController(view).navigate(action)
    }

    fun wordtoayarlar(view: View){
        val action = wordsDirections.actionWordsToAyarlar()
        Navigation.findNavController(view).navigate(action)
    }

}