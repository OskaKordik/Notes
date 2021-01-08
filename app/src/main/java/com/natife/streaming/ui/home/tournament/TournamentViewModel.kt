package com.natife.streaming.ui.home.tournament

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.match.Tournament

abstract class TournamentViewModel: BaseViewModel() {
    abstract val list: LiveData<List<Tournament>>
}

class TournamentViewModelImpl(private val tournaments: Array<Tournament>): TournamentViewModel() {
    override val list = MutableLiveData<List<Tournament>>()

    init {
        list.value = tournaments.toList()
    }

}