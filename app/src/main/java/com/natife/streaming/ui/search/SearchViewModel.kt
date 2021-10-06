package com.natife.streaming.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.search.SearchResult
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.SearchUseCase
import kotlinx.coroutines.Job

abstract class SearchViewModel : BaseViewModel() {
    abstract val resultsTeam: LiveData<List<SearchResult>>
    abstract val resultsPlayer: LiveData<List<SearchResult>>
    abstract val resultsTournament: LiveData<List<SearchResult>>
    abstract val setupTypeSearchResult: LiveData<SearchResult.Type>


    abstract fun search(text: String)
    abstract fun select(result: SearchResult)

}

class SearchViewModelImpl(
    private val searchUseCase: SearchUseCase,
    private val router: Router,
) : SearchViewModel() {
    override val resultsTeam = MutableLiveData<List<SearchResult>>()
    override val resultsPlayer = MutableLiveData<List<SearchResult>>()
    override val resultsTournament = MutableLiveData<List<SearchResult>>()
    override val setupTypeSearchResult = MutableLiveData<SearchResult.Type>()


//    override val sportLiveDate = MutableLiveData<String>()
//    override val genderLiveDate = MutableLiveData<String>()
//    override val typeLiveDate = MutableLiveData<String>()

    private var job: Job? = null
//    private var sourceList = listOf<SearchResult>()

//    private var sport: Int
//    private var gender: Int
//    private var type: Int


//    init {
//        launch {
//            tabList.value = typeUseCase.execute()
//        }


//
//
//        sport = prefs.getSport()
//        gender = prefs.getSport()
//        type = prefs.getType()
//        searchLocal("")
//
//        collectCatching(prefs.getGenderFlow()) {
//
//            launchCatching {
//                withContext(Dispatchers.Main) {
//                    gender = it
//                    genderLiveDate.value = when (gender) {
//                        1 -> "Мужчины"
//                        else -> "Женщины"
//                    }//TODO fast solution, multilang
//                    filter()
//                }
//            }
//        }
//        collectCatching(prefs.getSportFlow()) { sportId ->
//            sport = sportId
//
//            if (sportId < 0) {
//
//                launchCatching {
//                    sportLiveDate.value = "Любой"//TODO fast solution, multilang
//                }
//
//            }
//            launch {
//                sportLiveDate.value =
//                    sportUseCase.execute(reload = false).find { it.id == sportId }?.name
//            }
//            launchCatching {
//                withContext(Dispatchers.Main) {
//                    filter()
//                }
//            }
//        }
//        collectCatching(prefs.getTypeFlow()) {
//            //TODO fast solution, multilang
//            launchCatching {
//                withContext(Dispatchers.Main) {
//                    type = it
//
//                    typeLiveDate.value = when (type) {
//                        0 -> "Команда"
//                        1 -> "Игрок"
//                        else -> "Турнир"
//                    }
//                    filter()
//                }
//            }
//
//
//        }
//    }


    override fun search(text: String) {
        job?.cancel()
        job = launch {
            filter(searchUseCase.execute(text))
//            withContext(Dispatchers.Main) {
//                filter(it)
//            }

        }
    }

//    override fun searchLocal(text: String) {
//        job?.cancel()
//        job = launch {
//            delay(500)
//            sourceList = searchUseCase.execute(text, true)
//            withContext(Dispatchers.Main) {
//                filter()
//            }
//        }
//    }

    private fun filter(sourceList: List<SearchResult>) {
        launch {
            resultsTeam.value = sourceList.filter { it.type == SearchResult.Type.TEAM }
            resultsPlayer.value = sourceList.filter { it.type == SearchResult.Type.PLAYER }
            resultsTournament.value = sourceList.filter { it.type == SearchResult.Type.TOURNAMENT }

            setupTypeSearchResult.postValue(
                when {
                    resultsPlayer.value?.isNotEmpty() == true -> SearchResult.Type.PLAYER
                    resultsTeam.value?.isNotEmpty() == true -> SearchResult.Type.TEAM
                    resultsTournament.value?.isNotEmpty() == true -> SearchResult.Type.TOURNAMENT
                    else -> SearchResult.Type.PLAYER
            })
        }
    }

//    override fun showSportDialog() {
////        router.navigate(R.id.action_searchFragment_to_sportDialog2)
//    }
//
//    override fun showGenderDialog() {
////        router.navigate(R.id.action_searchFragment_to_genderDialog)
//    }
//
//    override fun showTypeDialog() {
////        router.navigate(R.id.action_searchFragment_to_typeDialog)
//    }

//    override fun back() {
//        router.navigateUp()
//    }

    override fun select(result: SearchResult) {
//        Timber.e("result $result")
//        launchCatching {
//            searchUseCase.execute(result)
//        }
        router.navigate(
            SearchFragmentDirections.actionSearchFragmentToTournamentFragment(
                result.sport,
                tournamentId = result.id,
                type = result.type
            )
        )


    }
}