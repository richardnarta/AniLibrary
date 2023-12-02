package com.example.anilibrary.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.anilibrary.databinding.FragmentPlannedListBinding
import com.example.anilibrary.model.data.database.AnimeListDatabase
import com.example.anilibrary.model.data.database.AnimeListEntity
import com.example.anilibrary.model.data.repository.AnimeListRepository
import com.example.anilibrary.ui.adapter.AnimeListAdapter
import com.example.anilibrary.viewmodel.DBViewModelFactory
import com.example.anilibrary.viewmodel.ListViewModel

class PlannedListFragment : Fragment() {
    private val database by lazy { AnimeListDatabase.getAnimeDB(requireContext()) }
    private val repository by lazy { AnimeListRepository(database.getDao()) }

    private var _binding: FragmentPlannedListBinding? = null
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
        _binding = FragmentPlannedListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        bindingListWidget()
        createRecyclerView()
        loadData()
        itemOnClickListener()
        createSearchView()

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
        viewModel.showPlannedAnimeByQuery("%%") {
            it.observe(viewLifecycleOwner){animeList->
                binding.emptyListScreen.root.isVisible = animeList.isEmpty()
                recyclerView.isVisible = animeList.isNotEmpty()
                animeAdapter.submitList(animeList)
            }
        }
    }

    private fun itemOnClickListener() {
        animeAdapter.setOnClickListener(object :
            AnimeListAdapter.OnClickListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onRemoveClick(position: Int, model: AnimeListEntity) {
                viewModel.deleteAnime(model.id!!)
                animeAdapter.notifyDataSetChanged()
            }

            override fun onCardClick(position: Int, model: AnimeListEntity) {
                findNavController().navigate(PlannedListFragmentDirections.actionNavigationPlannedListToNavigationDetail(model.id!!))
            }
        })
    }

    private fun createSearchView() {
        searchBar.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    viewModel.showPlannedAnimeByQuery("%$newText%") {
                        it.observe(viewLifecycleOwner){animeList->
                            animeAdapter.submitList(animeList)
                        }
                    }
                }
                return false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}