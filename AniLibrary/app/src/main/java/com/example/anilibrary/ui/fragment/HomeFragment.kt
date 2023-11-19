package com.example.anilibrary.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.anilibrary.databinding.FragmentHomeBinding
import com.example.anilibrary.model.data.pojo.AnimeNode
import com.example.anilibrary.ui.adapter.SeasonAnimePagingAdapter
import com.example.anilibrary.viewmodel.HomeViewModel
import com.example.anilibrary.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel:HomeViewModel by viewModels{
        ViewModelFactory(this)
    }

    private lateinit var animeAdapter: SeasonAnimePagingAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var seasonTitle: TextView
    private lateinit var seasonYear: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        bindingHomeWidget()
        createRecyclerView()
        loadData()
        animeCardOnClickListener()

        return root
    }

    private fun bindingHomeWidget(){
        binding.apply {
            recyclerView = rvAnimeCard
            seasonTitle = tvSeason
            seasonYear = tvYear
        }
    }

    private fun createRecyclerView(){
        animeAdapter = SeasonAnimePagingAdapter()

        recyclerView.apply {
            adapter = animeAdapter
        }
    }

    private fun loadData(){
        seasonTitle.text = viewModel.default[0].capitalize()
        seasonYear.text = viewModel.default[1]

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.animeFlow.observe(viewLifecycleOwner){ pagingData->
                animeAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
            }
        }
    }

    private fun animeCardOnClickListener(){
        animeAdapter.setOnClickListener(object :
        SeasonAnimePagingAdapter.OnClickListener{
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