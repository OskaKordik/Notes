package com.natife.streaming.ui.mypreferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.natife.streaming.API_TRANSLATE
import com.natife.streaming.R
import com.natife.streaming.api.MainApi
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.dto.sports.SportTranslateDTO
import com.natife.streaming.data.dto.sports.toSportTranslateDTO
import com.natife.streaming.data.dto.tournament.TournamentTranslateDTO
import com.natife.streaming.data.request.BaseRequest
import com.natife.streaming.data.request.TranslateRequest
import com.natife.streaming.db.entity.PreferencesSport
import com.natife.streaming.db.entity.PreferencesTournament
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.GetSportUseCase
import com.natife.streaming.usecase.GetTournamentUseCase
import com.natife.streaming.usecase.SaveSportUseCase
import com.natife.streaming.usecase.SaveTournamentUseCase


abstract class UserPreferencesViewModel : BaseViewModel() {
    abstract val allUserPreferencesInTournament: LiveData<List<PreferencesTournament>>
    abstract val allUserPreferencesInSport: LiveData<List<PreferencesSport>>
    abstract val sportsSelected: LiveData<SportTranslateDTO>
    abstract val sportsList: LiveData<List<SportTranslateDTO>>

    abstract fun applyMypreferencesClicked()
    abstract fun findClicked()
    abstract fun kindOfSportClicked(sport: SportTranslateDTO)
    abstract fun kindOfSportSelected(sport: SportTranslateDTO?)
    abstract fun listOfTournamentsClicked(tournament: TournamentTranslateDTO)
    abstract fun onFinishClicked()
    abstract fun getTranslateLexic(sports: List<PreferencesSport>, lang: String)
}

class UserPreferencesViewModelImpl(
    private val getSportUseCase: GetSportUseCase,
    private val saveSportUseCase: SaveSportUseCase,
    private val tournamentUseCase: GetTournamentUseCase,
    private val saveTournamentUseCase: SaveTournamentUseCase,
    private val api: MainApi,
    private val router: Router
) : UserPreferencesViewModel() {
    override val sportsList = MutableLiveData<List<SportTranslateDTO>>()
    override val sportsSelected = MutableLiveData<SportTranslateDTO>()
    override val allUserPreferencesInTournament: LiveData<List<PreferencesTournament>>
        get() = tournamentUseCase.getAllUserPreferencesInTournamentFlow().asLiveData()
    override val allUserPreferencesInSport: LiveData<List<PreferencesSport>>
        get() = getSportUseCase.getAllUserPreferencesInSportFlow().asLiveData()

    override fun applyMypreferencesClicked() {
        router.navigate(R.id.action_global_nav_main)
    }

    override fun findClicked() {
//        TODO("Not yet implemented")
    }

    override fun kindOfSportClicked(sport: SportTranslateDTO) {
        launch {
            saveSportUseCase.setSportCheckUncheck(sport)
        }
    }

    override fun kindOfSportSelected(sport: SportTranslateDTO?) {
        sportsSelected.value = sport
    }

    override fun listOfTournamentsClicked(tournament: TournamentTranslateDTO) {
        launch {
            saveTournamentUseCase.setTournamentCheckUncheck(tournament)
        }
    }

    override fun onFinishClicked() {
        router.navigate(R.id.action_global_nav_main)
    }

    override fun getTranslateLexic(
        sports: List<PreferencesSport>,
        lang: String
    ) {
        launch {
            val translates =
                api.getTranslate(BaseRequest(procedure = API_TRANSLATE, params = TranslateRequest(
                    language = lang.toLowerCase(),
                    params = sports.map { it.lexic }
                )))
            sportsList.value = sports.toSportTranslateDTO(translates)
        }
    }
}

