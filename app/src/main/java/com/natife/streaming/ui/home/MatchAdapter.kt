package com.natife.streaming.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.natife.streaming.R
import com.natife.streaming.base.BaseListAdapter
import com.natife.streaming.base.BasePagedListAdapter
import com.natife.streaming.data.match.Match
import timber.log.Timber

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
        Timber.e("JNONOIDNODN item ${oldItem.id == newItem.id}")
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Match, newItem: Match): Boolean {
        Timber.e("JNONOIDNODN item1 ${oldItem == newItem}")
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Match, newItem: Match): Any? {

        val payloads = mutableListOf<Any>()
        if (oldItem.image != newItem.image){
            Timber.e("JNONOIDNODN pay image")
            payloads.add(IMAGE_PAYLOAD)
        }
        if (oldItem.info != oldItem.info){
            Timber.e("JNONOIDNODN pay info")
            payloads.add(INFO_PAYLOAD)
        }
        if (oldItem.sportName != newItem.sportName){
            Timber.e("JNONOIDNODN pay sportName")
            payloads.add(SPORT_PAYLOAD)
        }

        return  payloads
    }

    object IMAGE_PAYLOAD
    object INFO_PAYLOAD
    object SPORT_PAYLOAD

}