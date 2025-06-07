package com.example.languify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.languify.databinding.FragmentLearnedWordsReportBinding
import kotlinx.coroutines.launch


class LearnedWordsReportFragment : Fragment() {

    private var _binding: FragmentLearnedWordsReportBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WordViewModel by viewModels()
    private lateinit var adapter: WordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLearnedWordsReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = WordAdapter()
        binding.recyclerLearnedReport.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerLearnedReport.adapter = adapter

        val userId = requireActivity().getSharedPreferences("user_prefs", 0).getInt("userId", -1)

        lifecycleScope.launch {
            val db = UserDatabase.getDatabase(requireContext())
            val learnedWords = db.WordProgressDao().getLearnedWordsFull(userId)
            adapter.submitList(learnedWords)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
