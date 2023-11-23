package com.example.anilibrary.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.anilibrary.databinding.FragmentExploreBinding
import com.example.anilibrary.model.data.pojo.AnimeNode
import com.example.anilibrary.ui.adapter.ExploreLoadingStateAdapter
import com.example.anilibrary.ui.adapter.LoadingAdapter
import com.example.anilibrary.ui.adapter.SearchAnimePagingAdapter
import com.example.anilibrary.viewmodel.ExploreViewModel
import com.example.anilibrary.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch


class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExploreViewModel by viewModels{
        ViewModelFactory()
    }

    private lateinit var animeAdapter: SearchAnimePagingAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: SearchView

    private lateinit var loadingAdapter: LoadingAdapter
    private lateinit var loadingRecyclerView: RecyclerView

    private lateinit var reloadButton: Button
    private lateinit var welcomeScreen: LinearLayout
    private var firstInput = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        bindingExploreWidget()
        createRecyclerView()
        loadingView()
        loadData()
        createSearchView()
        animeCardOnClickListener()

        return root
    }

    private fun bindingExploreWidget(){
        binding.apply {
            recyclerView = rvAnimeCard
            searchBar = searchView
            loadingRecyclerView = rvAnimeCardSkeleton
            reloadButton = retryButton
            welcomeScreen = infoExplore
        }
    }

    private fun createRecyclerView(){
        animeAdapter = SearchAnimePagingAdapter(this)

        recyclerView.apply {
            adapter = animeAdapter.withLoadStateFooter(
                footer = ExploreLoadingStateAdapter{
                    animeAdapter.retry()
                }
            )
        }

        loadingAdapter = LoadingAdapter(0, arrayOf(0,1,2,3,4,5,6,7,8,9))
        loadingRecyclerView.apply {
            adapter = loadingAdapter
        }
    }

    private fun loadingView(){
        animeAdapter.addLoadStateListener { state->
            binding.shimmerView.isVisible = state.source.refresh is LoadState.Loading
            loadingRecyclerView.isVisible = state.source.refresh is LoadState.Loading
            recyclerView.isVisible = state.source.refresh is LoadState.NotLoading && animeAdapter.itemCount > 0
            welcomeScreen.isVisible = state.source.refresh is LoadState.NotLoading && firstInput
        }

        reloadButton.setOnClickListener {
            animeAdapter.retry()
        }
    }

    private fun loadData(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.animeFlow.observe(viewLifecycleOwner){ pagingData->
                if(viewModel.getCurrentQuery() != ""){
                    animeAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
                }
            }
        }
    }

    private fun createSearchView(){
        searchBar.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null){
                    recyclerView.scrollToPosition(0)
                    viewModel.searchAnime(query)
                    searchBar.clearFocus()
                    firstInput = false
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun animeCardOnClickListener(){
        animeAdapter.setOnClickListener(object :
            SearchAnimePagingAdapter.OnClickListener{
            override fun onClick(position: Int, model: AnimeNode) {
                Log.d("ID anime","${model.id}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}