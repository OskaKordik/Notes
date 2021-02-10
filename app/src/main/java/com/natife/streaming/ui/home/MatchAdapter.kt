package com.natife.streaming.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.natife.streaming.R
import com.natife.streaming.base.BaseListAdapter
import com.natife.streaming.base.BasePagedListAdapter
import com.natife.streaming.data.match.Match

class MatchAdapter: BaseListAdapter<Match, MatchViewHolder>(MatchDiffUtil()) {

    var onBind: ((Int)->Unit)? = null
    var onClick: ((Match)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        return MatchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_match, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (position> itemCount-20)
        onBind?.invoke(position)
        holder.itemView.setOnClickListener {
            onClick?.invoke(currentList[position])
        }
    }
}
class MatchDiffUtil: DiffUtil.ItemCallback<Match>() {
    override fun areItemsTheSame(oldItem: Match, newItem: Match): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Match, newItem: Match): Boolean {
        return oldItem == newItem
    }

}