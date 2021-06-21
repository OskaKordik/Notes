package com.natife.streaming.ui.player.menu.quality

import android.view.LayoutInflater
import android.view.ViewGroup
import com.natife.streaming.R
import com.natife.streaming.base.BaseAdapter
import com.natife.streaming.base.BaseViewHolder

class VideoQualityAdapter(private val onQualityClickListener: ((quality: String) -> Unit)) :
    BaseAdapter<String>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
        return VideoQualityViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_dialog_choose_quality, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<String>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            onQualityClickListener.invoke(list[position])
        }
    }
}
