package com.natife.streaming.ui.home.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.usecase.GetShowScoreUseCase
import com.natife.streaming.usecase.SaveShowScoreUseCase

abstract class ScoreViewModel : BaseViewModel() {
    abstract val title: LiveData<String>
    abstract val showScore: LiveData<List<String>>
    abstract fun select(position: Int)
}

class ScoreViewModelImpl(
    private val getShowScoreUseCase: GetShowScoreUseCase,
    private val saveShowScoreUseCase: SaveShowScoreUseCase
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