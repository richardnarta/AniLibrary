package com.example.anilibrary.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anilibrary.R
import com.example.anilibrary.databinding.FragmentHomeBinding
import com.example.anilibrary.model.data.pojo.AnimeNode
import com.example.anilibrary.ui.adapter.HomeLoadingStateAdapter
import com.example.anilibrary.ui.adapter.LoadingAdapter
import com.example.anilibrary.ui.adapter.SeasonAnimePagingAdapter
import com.example.anilibrary.viewmodel.HomeViewModel
import com.example.anilibrary.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel:HomeViewModel by viewModels{
        ViewModelFactory()
    }

    private lateinit var animeAdapter: SeasonAnimePagingAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var rvLayoutManager: GridLayoutManager
    private lateinit var seasonTitle: TextView

    private lateinit var loadingAdapter: LoadingAdapter
    private lateinit var loadingRecyclerView: RecyclerView
    private lateinit var reloadButton: Button
    private lateinit var bookMark: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        bindingHomeWidget()
        createRecyclerView()
        loadingView()
        loadData()
        itemOnClickListener()

        return root
    }

    private fun bindingHomeWidget(){
        binding.apply {
            recyclerView = rvAnimeCard
            seasonTitle = tvHomeTitle
            loadingRecyclerView = rvAnimeCardSkeleton
            reloadButton = retryButton
            bookMark = ivBookmark
        }
    }

    private fun createRecyclerView(){
        animeAdapter = SeasonAnimePagingAdapter(this)
        rvLayoutManager = GridLayoutManager(requireContext(), 2)

        recyclerView.apply {
            layoutManager = rvLayoutManager
            adapter = animeAdapter.withLoadStateFooter(
                footer = HomeLoadingStateAdapter{
                    animeAdapter.retry()
                }
            )
        }

        rvLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return if (animeAdapter.getItemViewType(position) == 1){
                    2
                }else{
                    1
                }
            }
        }

        loadingAdapter = LoadingAdapter(1, arrayOf(1,2,3,4,5,6))
        loadingRecyclerView.apply {
            adapter = loadingAdapter
        }
    }

    private fun loadingView(){
        animeAdapter.addLoadStateListener { state->
            binding.shimmerView.isVisible = state.source.refresh is LoadState.Loading
            loadingRecyclerView.isVisible = state.source.refresh is LoadState.Loading
            recyclerView.isVisible = state.source.refresh is LoadState.NotLoading && animeAdapter.itemCount > 0
        }

        reloadButton.setOnClickListener {
            animeAdapter.retry()
        }
    }

    private fun loadData(){
        seasonTitle.text = getString(R.string.home_title,
            viewModel.default[0].replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }, viewModel.default[1])

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.animeFlow.observe(viewLifecycleOwner){ pagingData->
                animeAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
            }
        }
    }

    private fun itemOnClickListener(){
        animeAdapter.setOnClickListener(object :
        SeasonAnimePagingAdapter.OnClickListener{
            override fun onClick(position: Int, model: AnimeNode) {
                findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToNavigationDetail(model.id!!))
            }
        })

        bookMark.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToNavigationAllList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}