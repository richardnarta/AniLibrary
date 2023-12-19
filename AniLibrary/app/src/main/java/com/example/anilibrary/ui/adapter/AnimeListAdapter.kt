package com.example.anilibrary.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.anilibrary.R
import com.example.anilibrary.databinding.RvItemExploreBlueBinding
import com.example.anilibrary.databinding.RvItemExploreRedBinding
import com.example.anilibrary.model.data.database.AnimeListEntity
import java.util.Locale

class AnimeListAdapter(private val fragment: Fragment) : ListAdapter<AnimeListEntity, RecyclerView.ViewHolder>(diffCallback){
    private val plannedCV = 0
    private val watchedCV = 1

    private var onClickListener: OnClickListener?=null

    companion object{
        val diffCallback = object: DiffUtil.ItemCallback<AnimeListEntity>(){
            override fun areItemsTheSame(oldItem: AnimeListEntity, newItem: AnimeListEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: AnimeListEntity, newItem: AnimeListEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    inner class BlueViewHolder(val binding: RvItemExploreBlueBinding) : RecyclerView.ViewHolder(binding.root)

    inner class RedViewHolder(val binding: RvItemExploreRedBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh = if (viewType == plannedCV) {
            RedViewHolder(RvItemExploreRedBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            BlueViewHolder(RvItemExploreBlueBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentAnime = getItem(position)

        holder.itemView.setOnClickListener {
            if(onClickListener!=null){
                if (currentAnime != null){
                    onClickListener!!.onCardClick(position, currentAnime)
                }
            }
        }

        if(holder is BlueViewHolder){
            holder.binding.apply {
                Glide.with(holder.itemView.context)
                    .load(currentAnime?.animePoster)
                    .transition(DrawableTransitionOptions.withCrossFade()).centerInside()
                    .into(ivImg)

                tvTitle.text = currentAnime?.animeTitle

                if(currentAnime?.animeSeason == "" || currentAnime?.animeSeason == null){
                    tvSeason.text = "??"
                }else{
                    tvSeason.text = currentAnime.animeSeason.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    }
                }

                if(currentAnime?.animeYear == null){
                    tvYear.text = "??"
                }else{
                    tvYear.text = currentAnime.animeYear.toString()
                }

                if(currentAnime?.animeRating != null){
                    tvRating.text = fragment.getString(R.string.rating, currentAnime.animeRating.toString())
                }else{
                    tvRating.text = fragment.getString(R.string.rating, "N/A")
                }

                holder.binding.ivBin.setOnClickListener {
                    if (onClickListener!=null){
                        if (currentAnime != null) {
                            onClickListener!!.onRemoveClick(position, currentAnime)
                        }
                    }
                }
            }
        }else if(holder is RedViewHolder){
            holder.binding.apply {
                Glide.with(holder.itemView.context)
                    .load(currentAnime?.animePoster)
                    .transition(DrawableTransitionOptions.withCrossFade()).centerInside()
                    .into(ivImg)

                tvTitle.text = currentAnime?.animeTitle

                if(currentAnime?.animeSeason == "" || currentAnime?.animeSeason == null){
                    tvSeason.text = "??"
                }else{
                    tvSeason.text = currentAnime.animeSeason.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    }
                }

                if(currentAnime?.animeYear == null){
                    tvYear.text = "??"
                }else{
                    tvYear.text = currentAnime.animeYear.toString()
                }

                if(currentAnime?.animeRating != null){
                    tvRating.text = fragment.getString(R.string.rating, currentAnime.animeRating.toString())
                }else{
                    tvRating.text = fragment.getString(R.string.rating, "N/A")
                }

                holder.binding.ivBin.setOnClickListener {
                    if (onClickListener!=null){
                        if (currentAnime != null) {
                            onClickListener!!.onRemoveClick(position, currentAnime)
                        }
                    }
                }
            }
        }
    }

    interface OnClickListener{
        fun onRemoveClick(position: Int, model: AnimeListEntity)
        fun onCardClick(position: Int, model: AnimeListEntity)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    override fun getItemViewType(position: Int): Int {
        return if(getItem(position).listType == "watched"){
            watchedCV
        }else{
            plannedCV
        }
    }
}