package com.natife.streaming.ui.mypreferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.natife.streaming.API_TRANSLATE
import com.natife.streaming.R
import com.natife.streaming.api.MainApi
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.dto.sports.SportTranslateDTO
import com.natife.streaming.data.dto.sports.toSportTranslateDTO
import com.natife.streaming.data.dto.tournament.TournamentTranslateDTO
import com.natife.streaming.data.dto.tournament.toTournamentTranslateDTO
import com.natife.streaming.data.request.BaseRequest
import com.natife.streaming.data.request.TranslateRequest
import com.natife.streaming.db.entity.PreferencesSport
import com.natife.streaming.db.entity.PreferencesTournament
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.GetSportUseCase
import com.natife.streaming.usecase.GetTournamentUseCase
import com.natife.streaming.usecase.SaveSportUseCase
import com.natife.streaming.usecase.SaveTournamentUseCase
import com.natife.streaming.utils.combineAndCompute

//new
abstract class MypreferencesViewModel : BaseViewModel() {
    abstract val combineSportsAndPreferencesInSportData: MediatorLiveData<List<SportTranslateDTO>>
    abstract val combineTournamentAndPreferencesTournamentData: MediatorLiveData<List<TournamentTranslateDTO>>


    abstract fun applyMypreferencesClicked()
    abstract fun findClicked()
    abstract fun kindOfSportClicked(sport: SportTranslateDTO, isCheck: Boolean)
    abstract fun listOfTournamentsClicked(tournament: TournamentTranslateDTO, isCheck: Boolean)
    abstract fun initialization(lang: String)
    abstract fun onFinishClicked()
}

class MypreferencesViewModelImpl(
    private val getSportUseCase: GetSportUseCase,
    private val saveSportUseCase: SaveSportUseCase,
    private val tournamentUseCase: GetTournamentUseCase,
    private val saveTournamentUseCase: SaveTournamentUseCase,
    private val api: MainApi,
    private val router: Router,
) : MypreferencesViewModel() {
    private val sportsList = MutableLiveData<List<SportTranslateDTO>>()
    private val tournamentList = MutableLiveData<List<TournamentTranslateDTO>>()
    private val allUserPreferencesInTournament: LiveData<List<PreferencesTournament>>
        get() = tournamentUseCase.getAllUserPreferencesInTournament().asLiveData()
    private val allUserPreferencesInSport: LiveData<List<PreferencesSport>>
        get() = getSportUseCase.getAllUserPreferencesInSport().asLiveData()
    override val combineSportsAndPreferencesInSportData = sportsList.combineAndCompute(allUserPreferencesInSport){ a, b ->
        combimneSportsAndPreferencesInSport(a, b)
        }
    override val combineTournamentAndPreferencesTournamentData = tournamentList.combineAndCompute(allUserPreferencesInTournament){ a, b ->
        combineTournamentAndPreferencesTournament(a, b)
    }


    override fun applyMypreferencesClicked() {
        router.navigate(R.id.action_global_nav_main)
    }

    override fun findClicked() {
//        TODO("Not yet implemented")
    }

    override fun kindOfSportClicked(sport: SportTranslateDTO, isCheck: Boolean) {
        launch {
            saveSportUseCase.execute(sport, isCheck)
        }
    }

    override fun listOfTournamentsClicked(tournament: TournamentTranslateDTO, isCheck: Boolean) {
        launch {
            val s = tournamentList.value as MutableList
            if (isCheck) {
                val index = s.indexOfFirst { it.id == tournament.id && it.sport == tournament.sport && it.tournamentType == tournament.tournamentType}
                s[index] = s[index].copy(isCheck = true)
            } else{
                val index = s.indexOfFirst { it.id == tournament.id && it.sport == tournament.sport && it.tournamentType == tournament.tournamentType}
                s[index] = s[index].copy(isCheck = false)
            }
            tournamentList.value = s
            saveTournamentUseCase.execute(tournament, isCheck)
        }
    }

    override fun initialization(lang: String) {
        launch {
            initListOfSports(lang)
            initListOfTournament(lang)
        }
    }

    override fun onFinishClicked() {
        router.navigate(R.id.action_global_nav_main)
    }

    private suspend fun initListOfTournament(lang: String) {
        val tournament = tournamentUseCase.execute()
            .toTournamentTranslateDTO(lang)
            .sortedBy { it.id } as MutableList

        tournamentList.value = tournament
    }

    private suspend fun initListOfSports(lang: String) {
        val sports = getSportUseCase.execute()
        val translates =
            api.getTranslate(BaseRequest(procedure = API_TRANSLATE, params = TranslateRequest(
                language = lang,
                params = sports.map { it.lexic }
            )))
        sportsList.value = sports.toSportTranslateDTO(translates)
    }

    private fun combimneSportsAndPreferencesInSport(
        sport: List<SportTranslateDTO>,
        prefSport: List<PreferencesSport>
    ): List<SportTranslateDTO> {
        val sportCheck = sport as MutableList
        //check element
        prefSport.forEach { pref ->
            val index = sport.indexOfFirst { it.id == pref.id }
            sport[index] = sport[index].copy(isCheck = true)
        }
        return sportCheck
    }

    private fun combineTournamentAndPreferencesTournament(
        tournament: List<TournamentTranslateDTO>,
        prefTournament: List<PreferencesTournament>
    ): List<TournamentTranslateDTO> {
        val tournamentCheck = tournament as MutableList
        //check element
        prefTournament.forEach { pref ->
            val index = tournamentCheck.indexOfFirst { it.id == pref.id && it.sport == pref.sport && it.tournamentType == pref.tournamentType}
            tournamentCheck[index] = tournamentCheck[index].copy(isCheck = true)
        }
        return tournamentCheck
    }
}
