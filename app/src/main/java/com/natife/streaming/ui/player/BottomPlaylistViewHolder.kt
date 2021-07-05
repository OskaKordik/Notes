package com.natife.streaming.ui.player

import android.annotation.SuppressLint
import android.view.View
import androidx.leanback.widget.BaseGridView
import com.natife.streaming.base.BaseViewHolder
import com.natife.streaming.data.matchprofile.Episode
import kotlinx.android.synthetic.main.item_playlist.view.*

class BottomPlaylistViewHolder(view: View,private val onClick:((Episode,List<Episode>)->Unit) ): BaseViewHolder<Pair<String, List<Episode>>>(view) {
    @SuppressLint("RestrictedApi")
    override fun onBind(data: Pair<String, List<Episode>>) {
        itemView.playlistTitle.text = data.first
        val adapter = PlaylistAdapter(onClick)
        itemView.playlistList.adapter = adapter
        adapter.submitList(data.second.sortedBy { it.startMs })
        itemView.playlistList.focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ITEM
    }
}