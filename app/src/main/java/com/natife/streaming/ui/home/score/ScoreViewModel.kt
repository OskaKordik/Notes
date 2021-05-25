package com.natife.streaming.ui.home.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.GetShowScoreUseCase
import com.natife.streaming.usecase.SaveShowScoreUseCase
@Deprecated("don'tuse")
abstract class ScoreViewModel : BaseViewModel() {
    abstract val title: LiveData<String>
    abstract val showScore: LiveData<List<String>>
    abstract fun select(position: Int)
}

@Deprecated("don'tuse")
class ScoreViewModelImpl(
    private val getShowScoreUseCase: GetShowScoreUseCase,
    private val saveShowScoreUseCase: SaveShowScoreUseCase,
    private val router: Router
) : ScoreViewModel() {
    override val title = MutableLiveData<String>()
    override val showScore = MutableLiveData<List<String>>()

    override fun select(position: Int) {
        launch {
            saveShowScoreUseCase.execute(position == 0)//first element is positive
            router?.navigateUp()
        }
    }

    init {
        //todo title localisation
        title.postValue("Показывать счет?")
        launch {
            val variants = getShowScoreUseCase.execute()
            showScore.value = variants
        }
    }


}