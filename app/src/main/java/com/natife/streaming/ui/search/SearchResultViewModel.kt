package com.natife.streaming.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.search.SearchResult


class SearchResultViewModel() : BaseViewModel() {

    private val _resultsTeam = MutableLiveData<List<SearchResult>>()
    val resultsTeam: LiveData<List<SearchResult>> = _resultsTeam

    private val _resultsPlayer = MutableLiveData<List<SearchResult>>()
    val resultsPlayer: LiveData<List<SearchResult>> = _resultsPlayer

    private val _resultsTournament = MutableLiveData<List<SearchResult>>()
    val resultsTournament: LiveData<List<SearchResult>> = _resultsTournament

    fun setResultsTeam(list: List<SearchResult>) {
        _resultsTeam.postValue(list)
    }

    fun setResultsPlayer(list: List<SearchResult>) {
        _resultsPlayer.postValue(list)
    }

    fun setResultsTournament(list: List<SearchResult>) {
        _resultsTournament.postValue(list)
    }
}