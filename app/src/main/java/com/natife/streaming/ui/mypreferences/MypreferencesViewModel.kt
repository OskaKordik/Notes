package com.natife.streaming.ui.mypreferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.GetSportUseCase
import com.natife.streaming.usecase.GetTournamentUseCase
import com.natife.streaming.usecase.SaveSportUseCase
import com.natife.streaming.usecase.SaveTournamentUseCase
import com.natife.streaming.utils.ResourceProvider

//new
abstract class MypreferencesViewModel : BaseViewModel() {
    abstract val sportsList: LiveData<List<SportTranslateDTO>>
    abstract val tournamentList: LiveData<List<TournamentTranslateDTO>>

    abstract fun applyMypreferencesClicked()
    abstract fun findClicked()
    abstract fun kindOfSportClicked(sport: SportTranslateDTO, isCheck: Boolean)
    abstract fun listOfTournamentsClicked(tournament: TournamentTranslateDTO, isCheck: Boolean)
    abstract fun initialization()
}

class MypreferencesViewModelImpl(
    private val getSportUseCase: GetSportUseCase,
    private val saveSportUseCase: SaveSportUseCase,
    private val resourceProvider: ResourceProvider,
    private val tournamentUseCase: GetTournamentUseCase,
    private val saveTournamentUseCase: SaveTournamentUseCase,
    private val api: MainApi,
    private val router: Router,
) : MypreferencesViewModel() {
    override val sportsList = MutableLiveData<List<SportTranslateDTO>>()
    override val tournamentList = MutableLiveData<List<TournamentTranslateDTO>>()


    override fun applyMypreferencesClicked() {
//        TODO("Not yet implemented")
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
            saveTournamentUseCase.execute(tournament, isCheck)
        }
    }

    override fun initialization() {
        launch {
            initListOfSports()
            initListOfTournament()
        }
    }

    private suspend fun initListOfTournament() {
        val allUserPreferencesInTournament = tournamentUseCase.getAllUserPreferencesInTournament()
        val tournament = tournamentUseCase.execute()
            .toTournamentTranslateDTO(resourceProvider.getString(R.string.lang)).sortedBy { it.id } as MutableList
        //check element
        allUserPreferencesInTournament?.forEach { pref->
//            val index = tournament.indexOfFirst { it.id == pref.id }
            val index = tournament.binarySearch{ pref.id }
            when {
                index > 0 ->{
                    tournament[index] = tournament[index].copy(isCheck = true)
                }
                else ->{}
            }
        }
        tournamentList.value = tournament
    }

    private suspend fun initListOfSports() {
        val sports = getSportUseCase.execute()
        val translates =
            api.getTranslate(BaseRequest(procedure = API_TRANSLATE, params = TranslateRequest(
                language = resourceProvider.getString(R.string.lang),
                params = sports.map { it.lexic }
            )))

        val allUserPreferencesInSport = getSportUseCase.getAllUserPreferencesInSport()
        val sportTranslate = sports.toSportTranslateDTO(translates) as MutableList
        //check element
        allUserPreferencesInSport?.forEach { pref ->
            val index = sportTranslate.indexOfFirst { it.id == pref.id }
            sportTranslate[index] = sportTranslate[index].copy(isCheck = true)
        }
        sportsList.value = sportTranslate
    }
}
