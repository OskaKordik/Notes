package com.natife.streaming.ui.home.sport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewHolder
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.Sport
import com.natife.streaming.usecase.GetSportUseCase
import com.natife.streaming.usecase.SaveSportUseCase

abstract class SportViewModel : BaseViewModel() {
    abstract val title: LiveData<String>
    abstract val sport: LiveData<List<Sport>>
    abstract fun select(position: Int)
}

class SportViewModelImpl(
    private val getSportUseCase: GetSportUseCase,
    private val saveSportUseCase: SaveSportUseCase
) : SportViewModel() {

    override val title = MutableLiveData<String>()
    override val sport = MutableLiveData<List<Sport>>()

    init {

        //todo title localisation
        title.postValue("Выберите спорт")

        launch {
            val sports = getSportUseCase.execute()
            sport.value = sports
        }
    }

    override fun select(position: Int) {
        sport.value?.get(position)?.let {
            launch {
                saveSportUseCase.execute(it.id)
                router?.navigateUp()
            }

        }
    }
}