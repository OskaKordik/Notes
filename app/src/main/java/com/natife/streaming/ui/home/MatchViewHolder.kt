package com.natife.streaming.ui.home

import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.text.color
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewHolder
import com.natife.streaming.data.match.Match
import com.natife.streaming.ext.url
import kotlinx.android.synthetic.main.item_match.view.*

class MatchViewHolder(view: View) : BaseViewHolder<Match>(view) {
    override fun onBind(data: Match) {
        itemView.matchImage.url(data.image)
        itemView.matchTitle.text = "${data.team1.nameRus} - ${data.team2.nameRus}"
        val span = SpannableStringBuilder()
        span.color(
            itemView.resources.getColor(R.color.text_accent, null)
        ) { append(data.sportName.toUpperCase()) }
        span.append(" ")
        span.color(itemView.resources.getColor(R.color.text_gray, null)) { append(data.info) }
        itemView.matchDescription.text = span
        if (!data.access) {
            //TODO show alert
        }
    }
}