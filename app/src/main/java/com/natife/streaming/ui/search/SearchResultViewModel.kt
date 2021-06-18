package com.natife.streaming.ui.search

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.search.SearchResult


class SearchResultViewModel() : BaseViewModel() {
    private val _startViewID = MutableLiveData<ArrayList<Int>>()
        .apply {
            postValue(
                arrayListOf(
                    View.NO_ID,
                    View.NO_ID,
                    View.NO_ID,
                    View.NO_ID,
                    View.NO_ID
                )
            )
        }
    val startViewID: LiveData<ArrayList<Int>> = _startViewID

    private val _resultsTeam = MutableLiveData<List<SearchResult>>()
    val resultsTeam: LiveData<List<SearchResult>> = _resultsTeam

    private val _resultsPlayer = MutableLiveData<List<SearchResult>>()
    val resultsPlayer: LiveData<List<SearchResult>> = _resultsPlayer

    private val _resultsTournament = MutableLiveData<List<SearchResult>>()
    val resultsTournament: LiveData<List<SearchResult>> = _resultsTournament

    private val _searchResultClicked = MutableLiveData<SearchResult>()
    val searchResultClicked: LiveData<SearchResult> = _searchResultClicked

    fun setResultsTeam(list: List<SearchResult>) {
        _resultsTeam.postValue(list)
    }

    fun setResultsPlayer(list: List<SearchResult>) {
        _resultsPlayer.postValue(list)
    }

    fun setResultsTournament(list: List<SearchResult>) {
        _resultsTournament.postValue(list)
    }

    fun setStartId(startId: ArrayList<Int>) {
        _startViewID.value = startId
    }

    fun resetStartId() {
        _startViewID.postValue(
            arrayListOf(
                View.NO_ID,
                View.NO_ID,
                View.NO_ID,
                View.NO_ID,
                View.NO_ID
            )
        )
    }

    fun searchResultClicked(searchResultClicked: SearchResult) {
        _searchResultClicked.value = searchResultClicked
    }
}