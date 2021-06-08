package com.natife.streaming.ui.favorites

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.match.Match
import com.natife.streaming.data.search.SearchResult
import com.natife.streaming.db.LocalSqlDataSourse
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.FavoritesUseCase
import com.natife.streaming.usecase.MatchUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

abstract class FavoriteViewModel : BaseViewModel() {


    abstract val favorites: LiveData<List<FavoritesAdapter.Favorite>>
    abstract val matches: LiveData<List<Match>>
    abstract fun onFavoriteSelected(searchResult: SearchResult)
    abstract fun loadNext()
    abstract fun goToProfile(match: Match)
}

class FavoriteViewModelImpl(
    private val favoritesUseCase: FavoritesUseCase,
    private val matchUseCase: MatchUseCase,
    private val router: Router,
    private val context: Context,
    private val localSqlDataSourse: LocalSqlDataSourse
) : FavoriteViewModel() {
    override val favorites = MutableLiveData<List<FavoritesAdapter.Favorite>>()
    override val matches = MutableLiveData<List<Match>>()
    private var process: Job? = null
    private var selected: SearchResult? = null
    private val showScore = MutableLiveData<Boolean>()

    override fun onFavoriteSelected(searchResult: SearchResult) {
        Timber.e("$searchResult")

        if (searchResult.id > 0) {
            router.navigate(
                FavoritesFragmentDirections.actionFavoritesFragmentToTournamentFragment(
                    searchResult.sport,
                    searchResult.id,
                    searchResult.type
                )
            )
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
            matchUseCase.load(MatchUseCase.Type.SIMPLE)

            isLoading.set(false)

        }


    }

    override fun goToProfile(match: Match) {
        router.navigate(
            FavoritesFragmentDirections.actionFavoritesFragmentToMatchProfileFragment(
                matchId = match.id,
                sportId = match.sportId
            )
        )
    }

    init {

        //tracking global settings
        launchCatching {
            withContext(Dispatchers.IO) {
                collect(localSqlDataSourse.getGlobalSettingsFlow()) { globalSetings ->
                    showScore.value = globalSetings?.showScore
                    matches.value?.let { matchList ->
                        matchList.map { match ->
                            match.copy(isShowScore = globalSetings?.showScore ?: false)
                        }.let {
                            matches.value = it
                        }
                    }
                }
            }
        }

        launchCatching {
            collect(matchUseCase.list) { matchList ->
                matchList.map { match ->
                    match.copy(isShowScore = showScore.value ?: false)
                }.let {
                    matches.value = it
                }
            }
        }

        launchCatching {
            loadNext()
            val _favorites = favoritesUseCase.execute()
            val groups = _favorites.groupBy { it.type }
            val list = mutableListOf<FavoritesAdapter.Favorite>()
            list.add(
                SearchResult(
                    name = context.resources.getString(R.string.all),
                    sport = -1,
                    gender = -1,
                    image = "",
                    placeholder = "",
                    id = -1,
                    type = SearchResult.Type.NON,
                    countryId = -1,
                    countryName = ""
                )
            )
            groups.keys.forEach {
                when (it) {
                    SearchResult.Type.PLAYER -> {
                        list.add(FavoritesAdapter.Header(text = context.resources.getString(R.string.players)))
                        list.addAll(groups[it]?.toList() ?: listOf())
                    }
                    SearchResult.Type.TEAM -> {
                        list.add(FavoritesAdapter.Header(text = context.resources.getString(R.string.commands)))
                        list.addAll(groups[it]?.toList() ?: listOf())
                    }
                    SearchResult.Type.TOURNAMENT -> {
                        list.add(FavoritesAdapter.Header(text = context.resources.getString(R.string.tournaments)))
                        list.addAll(groups[it]?.toList() ?: listOf())
                    }
                }
            }
            favorites.value = list
        }
    }


}