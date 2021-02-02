package com.natife.streaming.ui.matchprofile

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.match.Match
import com.natife.streaming.data.matchprofile.MatchInfo
import com.natife.streaming.data.matchprofile.Player
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.GetThumbnailUseCase
import com.natife.streaming.usecase.MatchProfileUseCase
import timber.log.Timber

abstract class MatchProfileViewModel : BaseViewModel() {
    abstract fun back()
    abstract fun goToSettings()

    abstract val team1: LiveData<List<Player>>
    abstract val team2: LiveData<List<Player>>
    abstract val info: LiveData<MatchInfo>
    abstract val title: LiveData<String>
    abstract val league: LiveData<String>
    abstract val thumbnails: LiveData<List<Bitmap?>>
}

class MatchProfileViewModelImpl(
    private val match: Match,
    private val matchProfileUseCase: MatchProfileUseCase,
    private val router: Router,
    private val getThumbnailUseCase: GetThumbnailUseCase
) : MatchProfileViewModel() {
    override fun back() {
        router.navigateUp()
    }

    override fun goToSettings() {
        router.navigate(MatchProfileFragmentDirections.actionMatchProfileFragmentToMatchSettingsFragment(sportId = match.sportId,match = match))
    }

    override val team1 = MutableLiveData<List<Player>>()
    override val team2 = MutableLiveData<List<Player>>()
    override val info = MutableLiveData<MatchInfo>()
    override val title = MutableLiveData<String>()
    override val league = MutableLiveData<String>()
    override val thumbnails = MutableLiveData<List<Bitmap?>>()


    init {

        title.value =  "${match.team1.name} ${match.team1.score}:${match.team2.score} ${match.team2.name}"
        league.value = match.info
        launch {
            val matchInfo = matchProfileUseCase.getMatchInfo(match.id, match.sportId)
            Timber.e(matchInfo.toString())
            info.value = matchInfo
            team1.value = matchInfo.players1
            team2.value = matchInfo.players2
          //  val thumb = getThumbnailUseCase.execute(matchInfo.highlights,matchId = match.id,sportId = match.sportId)
           // thumbnails.value = thumb
        }
    }


}