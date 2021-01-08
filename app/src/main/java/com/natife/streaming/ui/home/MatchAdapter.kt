package com.natife.streaming.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.natife.streaming.R
import com.natife.streaming.base.BasePagedListAdapter
import com.natife.streaming.data.match.Match

class MatchAdapter(private val onMatchClickListener: (match: Match?) -> Unit): BasePagedListAdapter<Match, MatchViewHolder>(MatchDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_match, parent, false)
        return MatchViewHolder(view).apply {
            itemView.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (getItem(position)?.hasVideo!!) {
                        onMatchClickListener.invoke(getItem(position))
                    }
                }
            }
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