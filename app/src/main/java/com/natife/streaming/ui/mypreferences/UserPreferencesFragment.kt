package com.natife.streaming.ui.mypreferences

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.leanback.widget.BaseGridView
import androidx.lifecycle.lifecycleScope
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.data.dto.tournament.TournamentTranslateDTO
import com.natife.streaming.db.entity.toTournamentTranslateDTO
import com.natife.streaming.ext.hideKeyboard
import com.natife.streaming.ext.predominantColorToGradient
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_mypreferences_new.*
import kotlinx.android.synthetic.main.fragment_mypreferences_new.load_progress
import kotlinx.android.synthetic.main.fragment_search_new.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class UserPreferencesFragment : BaseFragment<UserPreferencesViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_mypreferences_new
    private val sportAdapter by lazy {
        SportsAdapter(viewModel::kindOfSportClicked, viewModel::kindOfSportSelected)
    }
    private val tournamentAdapter by lazy {
        TournamentAdapter(viewModel::listOfTournamentsClicked)
    }
    private var temporalList: List<TournamentTranslateDTO>? = null
    var isSearchMode = false
    private var searchJob: Job? = null
    private var sortJob: Job? = null


    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Heading in the predominant team color
        topConstraintLayout.predominantColorToGradient("#CB312A")
        load_progress.visibility = View.VISIBLE
        kindsOfSportsRecyclerView.isFocusable = false
        kindsOfSportsRecyclerView.adapter = sportAdapter
        kindsOfSportsRecyclerView.setNumColumns(1)
        kindsOfSportsRecyclerView.focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ITEM


        listOfTournamentsRecyclerView.isFocusable = false
        listOfTournamentsRecyclerView.adapter = tournamentAdapter
        listOfTournamentsRecyclerView.setNumColumns(2)
        listOfTournamentsRecyclerView.focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ITEM

        search_pref_text_field?.editText?.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) v.hideKeyboard()
            }
        search_pref_text_field?.editText?.setOnEditorActionListener(
            TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    load_progress.visibility = View.VISIBLE
                    searchJob?.cancel()
                    searchJob = lifecycleScope.launch {
                        viewModel.findClicked(
                            search_pref_text_field?.editText?.text.toString(),
                            resources.getString(R.string.lang)
                        ).collectLatest {
                            tournamentAdapter.submitList(
                                it.toTournamentTranslateDTO(
                                    resources.getString(
                                        R.string.lang
                                    )
                                )
                            )
                        }
                    }

                    search_pref_text_field?.editText?.apply {
//                        text = null
                        this.clearFocus()
                    }
                    return@OnEditorActionListener true
                }
                false
            })

        subscribe(viewModel.allUserPreferencesInSport) {
            viewModel.getTranslateLexic(it, resources.getString(R.string.lang))
        }

        subscribe(viewModel.sportsList) {
            sportAdapter.submitList(it)
        }

        subscribe(viewModel.sportsSelected) { selected ->
            if (!isSearchMode) {
                sortJob?.cancel()
                sortJob = lifecycleScope.launch {
                    load_progress.visibility = View.VISIBLE
                    kindsOfSportsRecyclerView.scrollToPosition(selected.id - 1)
                    viewModel.sportsList.value?.forEach { s ->
                        kindsOfSportsRecyclerView.layoutManager?.findViewByPosition(s.id - 1)
                            ?.apply {
                                isSelected = s.id == selected.id
                            }
                    }
                    val list = temporalList?.filter { tournament ->
                        tournament.sport == selected.id
                    }

                    tournamentAdapter.submitList(list)
                    load_progress.visibility = View.GONE
                }
            } else sortJob?.cancel()
        }

        subscribe(viewModel.allUserPreferencesInTournament) {
            if (!isSearchMode) {
                sortJob?.cancel()
                sortJob = lifecycleScope.launch {
                    temporalList = it.toTournamentTranslateDTO(resources.getString(R.string.lang))
                    if (viewModel.sportsSelected.value != null) {
                        tournamentAdapter.submitList(temporalList?.filter { tournament ->
                            tournament.sport == viewModel.sportsSelected.value!!.id
                        })
                    } else {
                        tournamentAdapter.submitList(temporalList)
                    }
                    viewModel.kindOfSportSelected(viewModel.sportsSelected.value)
                    load_progress.visibility = View.GONE
                }
            } else sortJob?.cancel()
        }

        search_button.setOnClickListener {
            if (search_layout?.isVisible == true) {
                search_layout?.visibility = View.GONE
                isSearchMode = false
            } else {
                search_layout?.visibility = View.VISIBLE
                isSearchMode = true
            }
        }
        applay_button.setOnClickListener {
            viewModel.applyMypreferencesClicked()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.onFinishClicked()
        }
    }
}


