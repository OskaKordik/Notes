package com.natife.streaming.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.natife.streaming.R
import com.natife.streaming.base.BaseListAdapter
import com.natife.streaming.data.search.SearchResult

class SearchAdapter: BaseListAdapter<SearchResult,SearchViewHolder>(SearchDiffUtil()) {

    var onClick: ((SearchResult)->Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search,parent,false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            onClick?.invoke(currentList[position])
        }

    }
}
class SearchDiffUtil: DiffUtil.ItemCallback<SearchResult>() {
    override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
       return oldItem == newItem
    }

}