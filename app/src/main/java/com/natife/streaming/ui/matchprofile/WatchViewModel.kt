package com.natife.streaming.ui.matchprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.match.Match
import com.natife.streaming.router.Router

abstract class WatchViewModel: BaseViewModel() {
    abstract fun back()
    abstract val title: LiveData<String>
}

class WatchViewModelImpl(private val match: Match,
                         private val router: Router): WatchViewModel() {
    override fun back() {
        router.navigateUp()
    }

    override val title = MutableLiveData<String>()

    init {
        title.value =  "${match.team1.name} - ${match.team2.name}"
    }

}