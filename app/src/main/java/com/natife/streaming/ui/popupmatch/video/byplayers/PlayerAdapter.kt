package com.natife.streaming.ui.popupmatch.video.byplayers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.natife.streaming.R
import com.natife.streaming.base.BaseListAdapter
import com.natife.streaming.data.matchprofile.Player

class PlayerAdapter () : BaseListAdapter<Player, PlayerViewHolder>(PlayerDiffCallback()) {
    var onClick: ((Player)->Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_player, parent, false).apply {
                //layoutParams = ViewGroup.LayoutParams(itemWidth,ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        )
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            onClick?.invoke(currentList[position])
        }
    }
}


class PlayerDiffCallback : DiffUtil.ItemCallback<Player>() {
    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem == newItem
    }

}