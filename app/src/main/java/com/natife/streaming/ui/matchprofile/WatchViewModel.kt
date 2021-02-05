package com.natife.streaming.ui.matchprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.Video
import com.natife.streaming.data.match.Match
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.SecondUseCase

abstract class WatchViewModel: BaseViewModel() {
    abstract fun back()
    abstract val title: LiveData<String>
    abstract val second: LiveData<Int>
    abstract val startFrom: LiveData<Long>
}

class WatchViewModelImpl(private val match: Match,
                         private val videos: Array<Video>,
                         private val router: Router,
                        private val secondUseCase: SecondUseCase): WatchViewModel() {
    override fun back() {
        router.navigateUp()
    }

    override val title = MutableLiveData<String>()
    override val second = MutableLiveData<Int>()
    override val startFrom = MutableLiveData<Long>()

    init {
        title.value =  "${match.team1.name} - ${match.team2.name}"
        launch {
            secondUseCase.execute(matchId = match.id,sportId = match.sportId)?.let { second->
           startFrom.value = videos.groupBy { it.quality }.values.toList()[0].filter { it.period<second.half }.map { it.duration }.sum()+second.second
        }

        }
    }

}