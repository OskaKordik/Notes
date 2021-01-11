package com.natife.streaming.ui.home.tournament

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.match.Tournament
import com.natife.streaming.router.Router

abstract class TournamentDialogViewModel: BaseViewModel() {
    abstract val list: LiveData<List<Tournament>>
}

class TournamentDialogViewModelImpl(private val tournaments: Array<Tournament>,
                                    private val router: Router
): TournamentDialogViewModel() {
    override val list = MutableLiveData<List<Tournament>>()

    init {
        list.value = tournaments.toList()
    }

}