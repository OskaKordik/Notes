package com.natife.streaming.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.natife.streaming.R
import com.natife.streaming.base.BaseListAdapter
import com.natife.streaming.data.match.Match

class MatchAdapter : BaseListAdapter<Match, MatchViewHolderNew>(MatchDiffUtil()) {

    var onBind: ((Int) -> Unit)? = null
    var onClick: ((Match) -> Unit)? = null
    private var FOCUSE_FIRST_TIME = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolderNew {
        return MatchViewHolderNew(
            LayoutInflater.from(parent.context).inflate(R.layout.item_match_new, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MatchViewHolderNew, position: Int) {
        super.onBindViewHolder(holder, position)
        if (FOCUSE_FIRST_TIME && position == 0) {
            holder.itemView.requestFocus()
            FOCUSE_FIRST_TIME = false
        }
        if (position > itemCount - 20)
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

    override fun getChangePayload(oldItem: Match, newItem: Match): Any? {

        val payloads = mutableListOf<Any>()
        if (oldItem.image != newItem.image){
            payloads.add(IMAGE_PAYLOAD)
        }
        if (oldItem.info != oldItem.info){
            payloads.add(INFO_PAYLOAD)
        }
        if (oldItem.sportName != newItem.sportName){
            payloads.add(SPORT_PAYLOAD)
        }

        return  payloads
    }

    object IMAGE_PAYLOAD
    object INFO_PAYLOAD
    object SPORT_PAYLOAD

}