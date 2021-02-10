package com.natife.streaming.ui.player

import android.view.View
import com.natife.streaming.base.BaseViewHolder
import com.natife.streaming.data.matchprofile.Episode
import com.natife.streaming.ext.url
import kotlinx.android.synthetic.main.item_episode.view.*

class PlaylistViewHolder(view: View) : BaseViewHolder<Episode>(view) {
    override fun onBind(data: Episode) {
        itemView.matchImage.url(data.image,data.placeholder)
        itemView.matchTitle.text = data.title
    }
}