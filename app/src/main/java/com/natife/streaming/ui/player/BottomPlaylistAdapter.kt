package com.natife.streaming.ui.player

import android.view.LayoutInflater
import android.view.ViewGroup
import com.natife.streaming.R
import com.natife.streaming.base.BaseAdapter
import com.natife.streaming.base.BaseViewHolder
import com.natife.streaming.data.matchprofile.Episode


class BottomPlaylistAdapter(private val onClick:((Episode,List<Episode>)->Unit) ): BaseAdapter<Pair<String,List<Episode>>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<Pair<String, List<Episode>>> {
        return BottomPlaylistViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_playlist,parent,false),onClick)
    }
}