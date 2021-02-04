package com.natife.streaming.ui.search

import android.view.View
import com.natife.streaming.base.BaseViewHolder
import com.natife.streaming.data.search.SearchResult
import com.natife.streaming.ext.urlCircled
import kotlinx.android.synthetic.main.item_search.view.*

class SearchViewHolder(view: View): BaseViewHolder<SearchResult>(view) {
    override fun onBind(data: SearchResult) {
        itemView.icon.urlCircled(data.image,data.placeholder)
        itemView.name.text = data.name
    }
}