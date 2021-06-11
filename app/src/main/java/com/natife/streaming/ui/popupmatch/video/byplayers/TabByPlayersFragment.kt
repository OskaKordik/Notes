package com.natife.streaming.ui.popupmatch.video.byplayers

import android.os.Bundle
import android.view.View
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.base.EmptyViewModel
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ui.popupmatch.PopupSharedViewModel
import kotlinx.android.synthetic.main.fragment_match_profile.*


class TabByPlayersFragment : BaseFragment<EmptyViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_tab_by_players
    private val popupSharedViewModel: PopupSharedViewModel by navGraphViewModels(R.id.popupVideo)
    private val teamAdapter1: PlayerAdapter by lazy { PlayerAdapter() }
    private val teamAdapter2: PlayerAdapter by lazy { PlayerAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstTeamRecycler.layoutManager = GridLayoutManager(
            context, 5,
            GridLayoutManager.VERTICAL, false
        )
        firstTeamRecycler.adapter = teamAdapter1
        secondTeamRecycler.layoutManager = GridLayoutManager(
            context, 5,
            GridLayoutManager.VERTICAL, false
        )
        secondTeamRecycler.adapter = teamAdapter2

        subscribe(popupSharedViewModel.team1, teamAdapter1::submitList)
        subscribe(popupSharedViewModel.team2, teamAdapter2::submitList)

        teamAdapter1.onClick = {
            popupSharedViewModel.player(it)
        }
        teamAdapter2.onClick = {
            popupSharedViewModel.player(it)
        }
    }
}