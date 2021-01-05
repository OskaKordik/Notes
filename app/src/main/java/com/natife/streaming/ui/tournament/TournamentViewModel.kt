package com.natife.streaming.ui.tournament

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.Tournament
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.TournamentUseCase

class TournamentViewModel(
    private val router: Router,
    private val tournamentUseCase: TournamentUseCase
) : BaseViewModel() {

    private val _tournament = MutableLiveData<Tournament>()
    val tournament: LiveData<Tournament> = _tournament

    init {
        launch {
            _tournament.value = tournamentUseCase.execute()
        }
    }

    fun onScoreClicked() {
        router.toast("Счет")
    }

    fun addToFavorite() {
        router.toast("Добавить в избранное")
    }
}