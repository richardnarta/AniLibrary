package com.example.anilibrary.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.anilibrary.databinding.FragmentDetailBinding
import com.example.anilibrary.model.data.util.DetailAnimeSource
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.anilibrary.R
import com.example.anilibrary.viewmodel.DetailViewModel
import kotlinx.coroutines.launch
import java.util.Locale

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
        bindingDetailWidget(this)
        onClickListener()

        return root
    }

    private fun loadData(){
        viewLifecycleOwner.lifecycleScope.launch {
            binding.shimmerView.isVisible = true
            binding.skeletonView.root.isVisible = true
            viewModel.dataAnime.value = DetailAnimeSource.detailSource(args.id)
        }
    }

    private fun bindingDetailWidget(context: DetailFragment){
        viewModel.dataAnime.observe(viewLifecycleOwner){ dataAnime->
            if(dataAnime.id != null){
                binding.shimmerView.isVisible = false
                binding.skeletonView.root.isVisible = false
                binding.animeDetail.isVisible = true
                binding.apply {
                    tvTitle.text = dataAnime.title
                    if (dataAnime.altTitle?.en == "") {
                        tvAltTitleEn.isVisible = false
                    }
                    if (dataAnime.altTitle?.ja == "") {
                        tvAltTitleJp.isVisible = false
                    }
                    tvAltTitleEn.text = dataAnime.altTitle?.en
                    tvAltTitleJp.text = dataAnime.altTitle?.ja
                    if (dataAnime.rating == null) {
                        tvRating.text = getString(R.string.rating, "N/A")
                    } else {
                        tvRating.text = getString(R.string.rating, dataAnime.rating.toString())
                    }
                    tvUser.text = getString(R.string.users, dataAnime.numUsers.toString())
                    Glide.with(context)
                        .load(dataAnime?.mainPicture?.large)
                        .apply(RequestOptions().override(190, 265))
                        .transition(DrawableTransitionOptions.withCrossFade()).centerInside()
                        .into(ivImg)
                    tvSynopsis.text = dataAnime.synopsis
                    if (dataAnime.rank == null) {
                        tvRank.text = getString(R.string.rank, "N/A")
                    } else {
                        tvRank.text = getString(R.string.rank, dataAnime.rank.toString())
                    }
                    if (dataAnime.mediaType=="ona" ||
                        dataAnime.mediaType=="ova" ||
                        dataAnime.mediaType=="tv"){
                        tvType.text =  getString(R.string.type, dataAnime.mediaType!!.uppercase())
                    }else{
                        tvType.text = getString(R.string.type, dataAnime?.mediaType?.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        })
                    }
                    tvEpisode.text = getString(R.string.num_eps,dataAnime.numEpisodes.toString())
                    dataAnime.status = dataAnime.status?.replace("_", " ")
                    val status = dataAnime.status?.split(" ")
                    dataAnime.status = status?.joinToString(" ") {
                        it.replaceFirstChar { char ->
                            if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString()
                        }
                    }
                    tvStatus.text = getString(R.string.status, dataAnime.status)
                    tvSeason.text = getString(R.string.season,dataAnime.startSeason?.season?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    })
                    tvSeason.append(dataAnime.startSeason?.year.toString())

                    if (dataAnime.studio!!.isEmpty()) {
                        tvStudios.text = getString(R.string.studio, "")
                    } else {
                        for (index in dataAnime.studio!!.indices) {
                            if (index == 0) {
                                tvStudios.text = getString(R.string.studio, dataAnime.studio?.get(index)?.name)
                            } else {
                                tvStudios.append(getString(R.string.append, dataAnime.studio?.get(index)?.name))
                            }
                        }
                    }
                    for (index in dataAnime.genre!!.indices) {
                        if (index == 0) {
                            tvGenre.text = getString(R.string.genre, dataAnime.genre?.get(index)?.name)
                        } else {
                            tvGenre.append(getString(R.string.append, dataAnime.genre?.get(index)?.name))
                        }
                    }
                }
            }else{
                binding.animeDetail.isVisible = false
                Handler().postDelayed({
                    binding.shimmerView.isVisible = false
                    binding.skeletonView.root.isVisible = false
                    binding.errorMessage.isVisible = true
                }, 2000)
            }
        }
    }

    private fun onClickListener() {
        binding.retryButton.setOnClickListener {
            binding.errorMessage.isVisible = false
            loadData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}