package com.example.anilibrary.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.anilibrary.MainActivity
import com.example.anilibrary.databinding.FragmentDetailBinding
import com.example.anilibrary.model.data.util.DetailAnimeSource
import com.example.anilibrary.model.data.pojo.AnimeDetailResponseAPI
import com.example.anilibrary.viewmodel.DetailViewModel
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loadData()
        bindingDetailWidget()

        return root
    }

    private fun loadData(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dataAnime.value = DetailAnimeSource.detailSource(args.id)!!
        }
    }

    private fun bindingDetailWidget(){
        viewModel.dataAnime.observe(viewLifecycleOwner){ dataAnime->
            binding.apply {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}