package com.example.proje

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proje.databinding.FragmentLearnedWordsReportBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
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
            val learnedIds = db.WordProgressDao().getLearnedWordIds(userId)
            val words = db.WordDao().getWordsByIds(learnedIds)
            adapter.submitList(words)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
