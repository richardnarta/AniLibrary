package com.example.anilibrary.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.anilibrary.databinding.RvItemHomeBinding
import com.example.anilibrary.model.data.pojo.AnimeNode

class SeasonAnimePagingAdapter: PagingDataAdapter<AnimeNode, SeasonAnimePagingAdapter.AnimeViewHolder>(diffCallback) {

    private var onClickListener: OnClickListener?=null

    companion object{
        val diffCallback = object: DiffUtil.ItemCallback<AnimeNode>(){
            override fun areItemsTheSame(oldItem: AnimeNode, newItem: AnimeNode): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: AnimeNode, newItem: AnimeNode): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    inner class AnimeViewHolder(val binding : RvItemHomeBinding): RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val currentAnime = getItem(position)

        holder.binding.apply {
            Glide.with(holder.itemView.context)
                .load(currentAnime?.mainPicture?.large)
                .apply(RequestOptions().override(200, 500))
                .transition(DrawableTransitionOptions.withCrossFade()).centerInside()
                .into(ivImg)

            tvTitle.text = currentAnime?.title
            tvType.text = currentAnime?.mediaType
            tvEpisode.text = currentAnime?.numEpisodes.toString()
            tvRating.text = currentAnime?.rank.toString()
        }

        holder.itemView.setOnClickListener {
            if (onClickListener!=null){
                if (currentAnime != null) {
                    onClickListener!!.onClick(position, currentAnime)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        return AnimeViewHolder(RvItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    interface OnClickListener{
        fun onClick(position: Int, model: AnimeNode)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }
}