package com.natife.streaming.ui.search

import android.view.View
import com.natife.streaming.base.BaseViewHolder
import com.natife.streaming.data.search.SearchResult
import com.natife.streaming.ext.bindFlagImage
import com.natife.streaming.ext.bindSportImage
import kotlinx.android.synthetic.main.item_list_of_tournaments_new.view.*

class SearchViewHolder(view: View): BaseViewHolder<SearchResult>(view) {
    override fun onBind(data: SearchResult) {
//        itemView.icon.urlCircled(data.image,data.placeholder)
//        itemView.name.text = data.name
        itemView.flag_of_command_country_image_l.bindFlagImage(data.countryId)
        itemView.type_of_game_image_l.bindSportImage(data.sport)
        itemView.name_of_comand_text_l.text = data.name
        itemView.country_name_text_l.text = data.countryName
        itemView.check_image.visibility = View.GONE
    }
}