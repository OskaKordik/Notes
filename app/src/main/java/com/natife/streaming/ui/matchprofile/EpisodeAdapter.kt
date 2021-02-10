package com.natife.streaming.ui.matchprofile

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.natife.streaming.R
import com.natife.streaming.base.BaseListAdapter
import com.natife.streaming.data.matchprofile.Episode

class EpisodeAdapter: BaseListAdapter<Episode, EpisodeViewHolder>(EpisodeDiffUtil()) {
    var onClick: ((Episode)->Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        return EpisodeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_episode,parent,false))
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            onClick?.invoke(currentList[position])
        }
    }
}
class EpisodeDiffUtil: DiffUtil.ItemCallback<Episode>() {

    override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
        return false
    }

}