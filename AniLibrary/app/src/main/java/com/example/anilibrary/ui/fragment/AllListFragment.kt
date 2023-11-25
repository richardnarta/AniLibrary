package com.example.anilibrary.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.anilibrary.databinding.FragmentAllListBinding
import com.example.anilibrary.model.data.database.AnimeListDatabase
import com.example.anilibrary.model.data.database.AnimeListEntity
import com.example.anilibrary.model.data.repository.AnimeListRepository
import com.example.anilibrary.ui.adapter.AnimeListAdapter
import com.example.anilibrary.viewmodel.DBViewModelFactory
import com.example.anilibrary.viewmodel.ListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class AllListFragment : Fragment() {
    private val database by lazy { AnimeListDatabase.getAnimeDB(requireContext()) }
    private val repository by lazy { AnimeListRepository(database.getDao()) }

    private var _binding: FragmentAllListBinding? = null
    private val binding get() = _binding!!

    private lateinit var animeAdapter: AnimeListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: SearchView

    private val viewModel: ListViewModel by viewModels {
        DBViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        bindingListWidget()
        createRecyclerView()
        loadData()
        itemOnClickListener()

        return root
    }

    private fun bindingListWidget(){
        binding.apply {
            recyclerView = rvAnimeCard
            searchBar = searchView
        }
    }

    private fun createRecyclerView() {
        animeAdapter = AnimeListAdapter(this)
        recyclerView.apply {
            adapter = animeAdapter
        }
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allAnimeList.observe(viewLifecycleOwner){
                animeAdapter.submitList(it)
            }
        }
    }

    private fun itemOnClickListener() {
        animeAdapter.setOnClickListener(object :
            AnimeListAdapter.OnClickListener{
            override fun onClick(position: Int, model: AnimeListEntity) {
                viewModel.deleteAnime(model.id!!)
                animeAdapter.notifyItemRemoved(position+1)
            }
        })
    }
}