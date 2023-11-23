package com.example.anilibrary.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.anilibrary.databinding.HomeLoadingBinding

class HomeLoadingStateAdapter(private val retry: ()-> Unit): LoadStateAdapter<HomeLoadingStateAdapter.LoadingStateViewHolder>() {

    inner class LoadingStateViewHolder(private val binding: HomeLoadingBinding, retry: ()->Unit):
        RecyclerView.ViewHolder(binding.root){

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState){
            binding.apply {
                shimmerView.isVisible = loadState is LoadState.Loading
                load1.root.isVisible = loadState is LoadState.Loading
                load2.root.isVisible = loadState is LoadState.Loading
                errorMessage.isVisible = loadState is LoadState.Error
                retryButton.isVisible = loadState is LoadState.Error
            }
        }
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        return LoadingStateViewHolder(HomeLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false), retry)
    }
}