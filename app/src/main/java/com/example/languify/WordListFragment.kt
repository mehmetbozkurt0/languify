
package com.example.proje
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proje.R
import com.example.proje.WordAdapter
import com.example.proje.WordViewModel



class WordListFragment : Fragment() {

    private lateinit var viewModel: WordViewModel
    private lateinit var adapter: WordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_word_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = WordAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(requireActivity())[WordViewModel::class.java]

        viewModel.wordList.observe(viewLifecycleOwner) { wordList ->
            adapter.submitList(wordList)
        }




    }  override fun onResume() {
        super.onResume()
        viewModel.refreshWords(requireContext()) // ✔️ her dönüşte liste yenilensin
    }

    }


