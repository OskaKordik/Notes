package com.natife.streaming.ui.tournament

import android.view.LayoutInflater
import android.view.ViewGroup
import com.natife.streaming.R
import com.natife.streaming.base.BaseListAdapter
import com.natife.streaming.data.match.Match
import com.natife.streaming.ui.home.MatchDiffUtil
import com.natife.streaming.ui.home.MatchViewHolder

class TournamentAdapter: BaseListAdapter<Match, TournamentViewHolder>(MatchDiffUtil()) {

    var onBind: ((Int)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentViewHolder {
        return TournamentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_match, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TournamentViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (position> itemCount-20)
            onBind?.invoke(position)
    }
}