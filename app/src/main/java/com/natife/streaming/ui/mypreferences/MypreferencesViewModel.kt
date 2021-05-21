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
import timber.log.Timber
import java.util.*

//new
abstract class MypreferencesViewModel : BaseViewModel() {
    abstract val sportsList: LiveData<List<SportTranslateDTO>>
    abstract val tournament: LiveData<List<TournamentTranslateDTO>>

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
    override val tournament = MutableLiveData<List<TournamentTranslateDTO>>()


    override fun applyMypreferencesClicked() {
//        TODO("Not yet implemented")
    }

    override fun findClicked() {
//        TODO("Not yet implemented")
    }

    override fun kindOfSportClicked(sport: SportTranslateDTO, isCheck: Boolean) {
//        TODO("Not yet implemented")
    }

    override fun listOfTournamentsClicked(tournament: TournamentTranslateDTO, isCheck: Boolean) {
//        TODO("Not yet implemented")
    }

    override fun initialization() {
        launch {
            initListOfSports()
            initListOfTournament()
        }
    }

    private suspend fun initListOfTournament() {
//        Timber.tag("TAG").d(tournamentUseCase.execute().toString())

        tournament.value = tournamentUseCase.execute()
            .toTournamentTranslateDTO(resourceProvider.getString(R.string.lang))
    }

    private suspend fun initListOfSports() {
        val sports = getSportUseCase.execute()
        val translates =
            api.getTranslate(BaseRequest(procedure = API_TRANSLATE, params = TranslateRequest(
                language = resourceProvider.getString(R.string.lang),
                params = sports.map { it.lexic }
            )))
        val sportTranslate = sports.toSportTranslateDTO(translates)
        sportsList.value = sportTranslate
    }
}
