package com.natife.streaming.ui.home

import android.annotation.SuppressLint
import android.view.View
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewHolder
import com.natife.streaming.data.match.Match
import com.natife.streaming.ext.fromResponse
import com.natife.streaming.ext.toDisplay3
import com.natife.streaming.ext.url
import kotlinx.android.synthetic.main.item_match_new.view.*

open class MatchViewHolderNew(private val view: View) : BaseViewHolder<Match>(view) {
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBind(data: Match) {
        itemView.match_imageView.url(data.image, data.placeholder)

        if (data.live) {
            itemView.alert_textView.text = view.resources.getString(R.string.live)
            itemView.alert_textView.background =
                view.resources.getDrawable(R.drawable.alert_background, view.context.theme)
        } else {
            itemView.alert_textView.text =
                data.date.fromResponse().toDisplay3(view.resources.getString(R.string.lang))
            itemView.alert_textView.background =
                view.resources.getDrawable(R.drawable.time_background, view.context.theme)
        }

        itemView.first_team_textView.text = data.team1.name
        itemView.first_team_score_textView.text = data.team1.score.toString()

        itemView.second_team_textView.text = data.team2.name
        itemView.second_team_score_textView.text = data.team2.score.toString()

        itemView.tournament_name_text.text = data.tournament.name
    }
}