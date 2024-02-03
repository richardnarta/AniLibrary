package com.example.anilibrary.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anilibrary.MainActivity
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
    private lateinit var spinner: Spinner
    private lateinit var currentSeason: Array<String>

    private lateinit var loadingAdapter: LoadingAdapter
    private lateinit var loadingRecyclerView: RecyclerView
    private lateinit var reloadButton: Button
    private lateinit var bookMark: ImageView

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(activity is MainActivity){
            lifecycleScope.launch {
                (activity as MainActivity).supportActionBar?.show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        bindingHomeWidget()
        createRecyclerView()
        createSpinner()
        loadingView()
        loadData()
        itemOnClickListener()
        scrollUp()

        return root
    }

    private fun bindingHomeWidget(){
        binding.apply {
            recyclerView = rvAnimeCard
            seasonTitle = tvHomeTitle
            loadingRecyclerView = rvAnimeCardSkeleton
            reloadButton = retryButton
            bookMark = ivBookmark
            spinner = spinnerSeason
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

        loadingAdapter = LoadingAdapter(1, arrayOf(1,2,3,4))
        loadingRecyclerView.apply {
            adapter = loadingAdapter
        }
    }

    private fun createSpinner() {
        var firstOpen = true
        var year1 = viewModel.year.value?.get(0)!!
        var year2 = viewModel.year.value?.get(1)!!
        var season = viewModel.season.value!!
        val seasonWithYear: Array<Array<String>> = Array(4) { Array(4) { "" } }
        var itemAdapter: MutableList<String> = mutableListOf()
        var updatedPosition: Int

        if (!viewModel.currentSeason.value.contentEquals(viewModel.default)) {
            currentSeason = viewModel.currentSeason.value!!
        }

        fun bindYear() {
            if (season[0] == "Winter") {
                seasonWithYear[0][0] = season[0]
                seasonWithYear[0][1] = year1.toString()
                for (i in 1..3) {
                    seasonWithYear[i][0] = season[i]
                    seasonWithYear[i][1] = year2.toString()
                }
            } else {
                val currentIndex = season.indexOf("Fall")
                for (i in 0..<currentIndex) {
                    seasonWithYear[i][0] = season[i]
                    seasonWithYear[i][1] = year1.toString()
                }
                for (i in currentIndex..3) {
                    seasonWithYear[i][0] = season[i]
                    seasonWithYear[i][1] = year2.toString()
                }
            }
        }

        fun updateItemAdapter () {
            itemAdapter = seasonWithYear.map {
                it.joinToString(" ")
            }.toMutableList()
        }

        fun updateSeasonViewModel (pos: Int) {
            currentSeason = itemAdapter[pos].split(" ").toTypedArray()
            viewModel.setSeason(currentSeason)
        }

        bindYear()
        updateItemAdapter()

        val spinnerAdapter = ArrayAdapter(requireContext(), androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, itemAdapter)
        spinner.adapter = spinnerAdapter
        spinner.dropDownVerticalOffset = 100
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!firstOpen) {
                    if (position != 1) {
                        updatedPosition = updateSeason(position)
                        spinner.setSelection(updatedPosition)
                        updateSeasonViewModel(updatedPosition)
                    } else if (itemAdapter[0].contains("Winter 2024")) {
                        if (seasonTitle.text != "Fall - 2023"){
                            updateSeasonViewModel(1)
                        }
                    }
                } else {
                    firstOpen = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

            fun updateSeason(pos: Int): Int {
                val seasonTemp: MutableList<String> = mutableListOf()
                var position = 0
                if (pos > 1) {
                    for (i in pos-1..3) {
                        seasonTemp.add(season[i])
                    }
                    for (i in 0 ..pos-2) {
                        seasonTemp.add(season[i])
                    }
                    if(seasonTemp[0] == "Summer" || (seasonTemp[3] == "Summer" && pos == 3)) {
                        year1-=1
                        year2-=1
                    }
                    season = seasonTemp.toTypedArray()
                    viewModel.season.value = season
                    viewModel.year.value = arrayOf(year1, year2)
                    position = 1
                } else if (pos<1 && !itemAdapter[pos].contains("Winter 2024")) {
                    seasonTemp.add(season[3])
                    for (i in 0..2) {
                        seasonTemp.add(season[i])
                    }
                    if(seasonTemp[3] == "Winter") {
                        year1+=1
                        year2+=1
                    }
                    season = seasonTemp.toTypedArray()
                    viewModel.season.value = season
                    viewModel.year.value = arrayOf(year1, year2)
                    position = 1
                }
                bindYear()
                updateItemAdapter()
                spinnerAdapter.clear()
                spinnerAdapter.addAll(itemAdapter)
                spinnerAdapter.notifyDataSetChanged()
                return position
            }
        }
    }

    private fun loadingView(){
        animeAdapter.addLoadStateListener { state->
            binding.shimmerView.isVisible = state.source.refresh is LoadState.Loading
            loadingRecyclerView.isVisible = state.source.refresh is LoadState.Loading
            binding.errorMessage.isVisible = animeAdapter.itemCount == 0 && state.source.refresh is LoadState.Error
            recyclerView.isVisible = state.source.refresh is LoadState.NotLoading && animeAdapter.itemCount > 0
        }

        reloadButton.setOnClickListener {
            animeAdapter.retry()
        }
    }

    private fun loadData(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.animeFlow.observe(viewLifecycleOwner){ pagingData->
                animeAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
                seasonTitle.text = getString(
                    R.string.home_title,
                    viewModel.currentSeason.value?.get(0)?.replaceFirstChar { char ->
                        if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString()
                    }, viewModel.currentSeason.value?.get(1))
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

    private fun scrollUp(){
        if((activity as MainActivity).checkNavView()){
            (activity as MainActivity).navView.setOnNavigationItemReselectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        recyclerView.smoothScrollToPosition(0)
                    }
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}