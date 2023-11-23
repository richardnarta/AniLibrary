package com.example.anilibrary.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.example.anilibrary.MainActivity
import com.example.anilibrary.databinding.FragmentDetailBinding
import com.example.anilibrary.model.data.pagination.DetailAnimeSource
import com.example.anilibrary.model.data.pojo.AnimeDetailResponseAPI

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()
    private lateinit var dataAnime: AnimeDetailResponseAPI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        (activity as MainActivity).navView.isVisible = false

        loadData()
        bindingDetailWidget()

        return root
    }

    private fun bindingDetailWidget(){
        binding.apply {

        }
    }

    private fun loadData(){
        dataAnime = DetailAnimeSource.detailSource(args.id)!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).navView.isVisible = true
        _binding = null
    }
}