package com.natife.streaming.ui.mypreferences

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.leanback.widget.BaseGridView
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.db.entity.toTournamentTranslateDTO
import com.natife.streaming.ext.predominantColorToGradient
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.fragment_mypreferences_new.*


class UserPreferencesFragment : BaseFragment<UserPreferencesViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_mypreferences_new
    private val sportAdapter by lazy {
        SportsAdapter(viewModel::kindOfSportClicked)
    }
    private val tournamentAdapter by lazy {
        TournamentAdapter(viewModel::listOfTournamentsClicked)
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Heading in the predominant team color
        topConstraintLayout.predominantColorToGradient("#CB312A")
        viewModel.initialization(resources.getString(R.string.lang))


        kindsOfSportsRecyclerView.isFocusable = false
        kindsOfSportsRecyclerView.adapter = sportAdapter
        kindsOfSportsRecyclerView.setNumColumns(1)
        kindsOfSportsRecyclerView.focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ITEM


        listOfTournamentsRecyclerView.isFocusable = false
        listOfTournamentsRecyclerView.adapter = tournamentAdapter
        listOfTournamentsRecyclerView.setNumColumns(2)
        listOfTournamentsRecyclerView.focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ITEM

        subscribe(viewModel.allUserPreferencesInSport) {
            viewModel.getTranslateLexic(it, resources.getString(R.string.lang))
        }

        subscribe(viewModel.sportsList) {
            sportAdapter.submitList(it)
        }

        subscribe(viewModel.allUserPreferencesInTournament) {
            tournamentAdapter.submitList(it.toTournamentTranslateDTO(resources.getString(R.string.lang)))
        }

        search_button.setOnClickListener {
            viewModel.findClicked()
        }
        applay_button.setOnClickListener {
            viewModel.applyMypreferencesClicked()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.onFinishClicked()
        }
    }
}


