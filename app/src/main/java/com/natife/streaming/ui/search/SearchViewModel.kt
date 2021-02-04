package com.natife.streaming.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.search.SearchResult
import com.natife.streaming.preferenses.SearchPrefs
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.GenderUseCase
import com.natife.streaming.usecase.GetSportUseCase
import com.natife.streaming.usecase.SearchTypeUseCase
import com.natife.streaming.usecase.SearchUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber

abstract class SearchViewModel: BaseViewModel() {

    abstract val results: LiveData<List<SearchResult>>
    abstract val sportLiveDate: LiveData<String>
    abstract val genderLiveDate: LiveData<String>
    abstract val typeLiveDate: LiveData<String>

    abstract fun search(text: String)
    abstract fun searchLocal(text: String)
    abstract fun showSportDialog()
    abstract fun showGenderDialog()
    abstract fun showTypeDialog()
    abstract fun back()
    abstract fun select(result: SearchResult)

}

class SearchViewModelImpl(private val searchUseCase: SearchUseCase,
                          private val router: Router,
private val sportUseCase: GetSportUseCase,
private val genderUseCase: GenderUseCase,
private val typeUseCase: SearchTypeUseCase,
private val prefs: SearchPrefs): SearchViewModel() {

    override val sportLiveDate = MutableLiveData<String>()
    override val genderLiveDate = MutableLiveData<String>()
    override val typeLiveDate = MutableLiveData<String>()

    private var job: Job? = null
    private var sourceList = listOf<SearchResult>()

    private var sport: Int
    private var gender: Int
    private var type: Int

    override val results =  MutableLiveData<List<SearchResult>>()

    init {
        sport = prefs.getSport()
        gender = prefs.getSport()
        type = prefs.getType()
        searchLocal("")

        collectCatching(prefs.getGenderFlow()){

            launchCatching {
                withContext(Dispatchers.Main){
                    gender = it
                    genderLiveDate.value = when(gender){
                        1->"Мужчины"
                        else-> "Женщины"
                    }//TODO fast solution, multilang
                    filter()
                }
            }
        }
        collectCatching(prefs.getSportFlow()){sportId->
            sport = sportId
            launch {
                sportLiveDate.value = sportUseCase.execute().find { it.id == sportId }?.name
            }
            launchCatching {
                withContext(Dispatchers.Main){
                    filter()
                }
            }
        }
        collectCatching(prefs.getTypeFlow()){
          //TODO fast solution, multilang
            launchCatching {
                withContext(Dispatchers.Main){
                    type = it

                    typeLiveDate.value = when(type){
                        0-> "Команда"
                        1-> "Игрок"
                        else-> "Турнир"
                    }
                    filter()
                }
            }


        }
    }




    override fun search(text: String) {
        job?.cancel()
        job = launch {
            sourceList = searchUseCase.execute(text)
            withContext(Dispatchers.Main){
                filter()
            }

        }
    }

    override fun searchLocal(text: String) {
        job?.cancel()
        job = launch {
            delay(500)
            sourceList = searchUseCase.execute(text,true)
            withContext(Dispatchers.Main){
                filter()
            }
        }
    }

    private fun filter(){
        results.value = sourceList.filter {
            it.type == when(type){
                0-> SearchResult.Type.TEAM
                1-> SearchResult.Type.PLAYER
                else-> SearchResult.Type.TOURNAMENT
            }
                    && it.gender == gender
                    && if(sport != -1 ){ it.sport == sport}else{true}
        }
    }

    override fun showSportDialog() {
        router.navigate(R.id.action_searchFragment_to_sportDialog2)
    }

    override fun showGenderDialog() {
        router.navigate(R.id.action_searchFragment_to_genderDialog)
    }

    override fun showTypeDialog() {
        router.navigate(R.id.action_searchFragment_to_typeDialog)
    }

    override fun back() {
        router.navigateUp()
    }

    override fun select(result: SearchResult) {
        Timber.e("result $result")
        launchCatching {
            searchUseCase.execute(result)
        }
        if (result.type == SearchResult.Type.TOURNAMENT){
            router.navigate(SearchFragmentDirections.actionSearchFragmentToTournamentFragment(result.sport,tournamentId = result.id))
        }

    }
}