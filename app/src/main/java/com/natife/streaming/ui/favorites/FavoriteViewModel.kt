package com.natife.streaming.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.match.Match
import com.natife.streaming.data.search.SearchResult
import com.natife.streaming.datasource.MatchParams
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.FavoritesUseCase
import com.natife.streaming.usecase.MatchUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

abstract class FavoriteViewModel: BaseViewModel() {


    abstract val favorites: LiveData<List<FavoritesAdapter.Favorite>>
    abstract val matches: LiveData<List<Match>>
    abstract fun onFavoriteSelected(searchResult: SearchResult)
    abstract fun loadNext()
    abstract fun goToProfile(match: Match)
}
class FavoriteViewModelImpl(private val favoritesUseCase: FavoritesUseCase, private val matchUseCase: MatchUseCase,private val router: Router): FavoriteViewModel() {


    override val favorites = MutableLiveData<List<FavoritesAdapter.Favorite>>()
    override val matches = MutableLiveData<List<Match>>()
    private var process: Job? = null
    private var selected: SearchResult?=null

    override fun onFavoriteSelected(searchResult: SearchResult) {
        Timber.e("$searchResult")

        if (searchResult.id>0){
            router.navigate(FavoritesFragmentDirections.actionFavoritesFragmentToTournamentFragment(searchResult.sport,searchResult.id,searchResult.type))
        }


        launchCatching {
//            matchUseCase.prepare(MatchParams(date = null,
//            sportId = searchResult.sport,
//            pageSize = 60,
//            subOnly = false,
//            additionalId =searchResult.id))
            selected = searchResult
         //   loadNext()
        }
    }

    private val mutex = Mutex()
    private var isLoading = AtomicBoolean(false)
    override fun loadNext() {

        launch {
            mutex.withLock {

                if (isLoading.get()) {
                    return@launch
                }

            }

            isLoading.set(true)
             matchUseCase.load( MatchUseCase.Type.SIMPLE)

            isLoading.set(false)

        }


    }

    override fun goToProfile(match: Match) {
        router.navigate(FavoritesFragmentDirections.actionFavoritesFragmentToMatchProfileFragment(matchId = match.id,sportId = match.sportId))
    }

    init {

        launchCatching {
            collect(matchUseCase.list){
                matches.value = it
            }
        }

        launchCatching {
            loadNext()
            val _favorites = favoritesUseCase.execute()
            val groups = _favorites.groupBy { it.type }
            val list = mutableListOf<FavoritesAdapter.Favorite>()
            list.add(SearchResult(
                name = "Все",
                sport = -1,
                gender = -1,
                image = "",
                placeholder = "",
                id = -1,
                type = SearchResult.Type.NON
            ))
            groups.keys.forEach {
                when(it){
                    SearchResult.Type.PLAYER -> {
                        list.add(FavoritesAdapter.Header(text = "Игроки"))
                        list.addAll(groups[it]?.toList() ?: listOf())
                    }
                    SearchResult.Type.TEAM -> {
                        list.add(FavoritesAdapter.Header(text = "Команды"))
                        list.addAll(groups[it]?.toList() ?: listOf())
                    }
                    SearchResult.Type.TOURNAMENT -> {
                        list.add(FavoritesAdapter.Header(text = "Турниры"))
                        list.addAll(groups[it]?.toList() ?: listOf())
                    }
                }
            }
            favorites.value = list
        }
    }


}