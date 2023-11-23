package com.example.anilibrary.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.anilibrary.databinding.RvItemHomeSkeletonBinding

class LoadingAdapter (private var cv: Array<Int>) : RecyclerView.Adapter<LoadingAdapter.MyViewHolder>(){

    inner class MyViewHolder (private val binding: RvItemHomeSkeletonBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RvItemHomeSkeletonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return cv.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    }
}