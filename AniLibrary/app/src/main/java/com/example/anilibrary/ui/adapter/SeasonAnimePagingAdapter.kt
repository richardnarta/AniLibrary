package com.example.anilibrary.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.anilibrary.R
import com.example.anilibrary.databinding.RvItemHomeBinding
import com.example.anilibrary.model.data.pojo.AnimeNode
import com.example.anilibrary.ui.fragment.HomeFragment
import java.util.Locale

class SeasonAnimePagingAdapter(private val fragment: HomeFragment): PagingDataAdapter<AnimeNode, SeasonAnimePagingAdapter.AnimeViewHolder>(diffCallback) {

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
            if (currentAnime?.mediaType=="ona" ||
                currentAnime?.mediaType=="ova" ||
                currentAnime?.mediaType=="tv"){
                tvType.text = currentAnime.mediaType?.uppercase()
            }else{
                tvType.text = currentAnime?.mediaType?.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
            }

            if(currentAnime?.numEpisodes==0 || currentAnime?.numEpisodes==null){
                tvEpisode.text = fragment.getString(R.string.tv_episode, "??")
            }else{
                tvEpisode.text = fragment.getString(R.string.tv_episode, currentAnime.numEpisodes.toString())
            }

            if(currentAnime?.rank != null){
                tvRating.text = fragment.getString(R.string.rating, currentAnime.rank.toString())
            }else{
                tvRating.text = fragment.getString(R.string.rating, "?.?")
            }
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

    override fun getItemViewType(position: Int): Int {
        return if(position < itemCount && getItem(position) != null){
            0
        }else{
            1
        }
    }

    interface OnClickListener{
        fun onClick(position: Int, model: AnimeNode)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }
}