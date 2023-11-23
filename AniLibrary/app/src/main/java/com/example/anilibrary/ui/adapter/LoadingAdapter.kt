package com.example.anilibrary.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.anilibrary.databinding.RvItemExploreSkeletonBinding
import com.example.anilibrary.databinding.RvItemHomeSkeletonBinding

class LoadingAdapter (private val type:Int, private val cv: Array<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val horizontalCV = 0
    private val verticalCV = 1

    inner class VerticalViewHolder (binding: RvItemHomeSkeletonBinding) : RecyclerView.ViewHolder(binding.root)

    inner class HorizontalViewHolder (binding: RvItemExploreSkeletonBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh = if (viewType == verticalCV) {
            VerticalViewHolder(RvItemHomeSkeletonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            HorizontalViewHolder(RvItemExploreSkeletonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return cv.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(type == 0){
            horizontalCV
        }else{
            verticalCV
        }
    }
}