package com.natife.streaming.ui.home.tournament

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.match.Tournament
import com.natife.streaming.preferenses.SettingsPrefs
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.GetTournamentUseCase
import com.natife.streaming.usecase.SaveTournamentUseCase
import timber.log.Timber
@Deprecated("don'tuse")
abstract class TournamentDialogViewModel: BaseViewModel() {
    abstract val list: LiveData<List<Tournament>>
    abstract fun select(position: Int)
}

@Deprecated("don'tuse")
class TournamentDialogViewModelImpl(private val tournament:Array<Tournament>,
                                    private val tournamentUseCase: GetTournamentUseCase,
                                    private val settingsPrefs: SettingsPrefs,
                                    private val saveTournamentUseCase: SaveTournamentUseCase,
                                    private val router: Router
): TournamentDialogViewModel() {
    override val list = MutableLiveData<List<Tournament>>()

    override fun select(position: Int) {
        list.value?.get(position)?.let {
            Timber.e("${it.name}")
            saveTournamentUseCase.execute(it.id)
            router.navigateUp()
        }

    }

    init {
        launch {
            val sport = settingsPrefs.getSport()
            val data = tournamentUseCase.execute(sport)
            list.value = data
        }

    }

}