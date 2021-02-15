package com.natife.streaming.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.search.SearchResult
import com.natife.streaming.usecase.FavoritesUseCase
import timber.log.Timber

abstract class FavoriteViewModel: BaseViewModel() {


    abstract val favorites: LiveData<List<FavoritesAdapter.Favorite>>
    abstract fun onFavoriteSelected(searchResult: SearchResult)
}
class FavoriteViewModelImpl(private val favoritesUseCase: FavoritesUseCase): FavoriteViewModel() {


    override val favorites = MutableLiveData<List<FavoritesAdapter.Favorite>>()
    override fun onFavoriteSelected(searchResult: SearchResult) {

    }
    init {
        launch {
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