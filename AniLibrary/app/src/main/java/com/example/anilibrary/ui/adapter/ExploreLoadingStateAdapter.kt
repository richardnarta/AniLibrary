package com.example.anilibrary.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.anilibrary.databinding.ExploreLoadingBinding

class ExploreLoadingStateAdapter(private val retry: ()-> Unit): LoadStateAdapter<ExploreLoadingStateAdapter.LoadingStateViewHolder>() {

    inner class LoadingStateViewHolder(private val binding: ExploreLoadingBinding, retry: ()->Unit):
        RecyclerView.ViewHolder(binding.root){

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState){
            binding.apply {
                shimmerView.isVisible = loadState is LoadState.Loading
                load.root.isVisible = loadState is LoadState.Loading
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
        return LoadingStateViewHolder(ExploreLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false), retry)
    }
}