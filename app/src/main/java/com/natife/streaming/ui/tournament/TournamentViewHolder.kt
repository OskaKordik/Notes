package com.natife.streaming.ui.tournament

import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.text.color
import androidx.core.view.isVisible
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewHolder
import com.natife.streaming.custom.Alert
import com.natife.streaming.data.match.Match
import com.natife.streaming.ext.url
import com.natife.streaming.ui.home.MatchViewHolder
import kotlinx.android.synthetic.main.item_match.view.*

class TournamentViewHolder(view: View): MatchViewHolder(view){
    override fun onBind(data: Match) {
        super.onBind(data)
        itemView.imageBought.isVisible = data.subscribed
    }

}