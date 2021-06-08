package com.natife.streaming.ui.tournament

import android.view.View
import androidx.core.view.isVisible
import com.natife.streaming.data.match.Match
import com.natife.streaming.ui.home.MatchViewHolderNew
import kotlinx.android.synthetic.main.item_match_new.view.*

class TournamentViewHolder(view: View, private val onClick: ((Match) -> Unit)) :
    MatchViewHolderNew(view) {
    override fun onBind(data: Match) {
        super.onBind(data)
        itemView.paid_content_image.isVisible = data.subscribed
        itemView.setOnClickListener {
            onClick.invoke(data)
        }
    }

}