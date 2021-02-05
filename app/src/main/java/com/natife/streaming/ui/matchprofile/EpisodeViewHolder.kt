package com.natife.streaming.ui.matchprofile

import android.graphics.Bitmap
import android.view.View
import com.natife.streaming.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_episode.view.*


class EpisodeViewHolder(view: View): BaseViewHolder<Bitmap>(view) {
    override fun onBind(data: Bitmap) {
        itemView.matchImage.setImageBitmap(data)
    }
}