package com.natife.streaming.ui.home.sport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.Sport
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.GetSportUseCase
import com.natife.streaming.usecase.SaveSportUseCase
@Deprecated("don'tuse")
abstract class SportViewModel : BaseViewModel() {
    abstract val title: LiveData<String>
    abstract val sport: LiveData<List<Sport>>
    abstract fun select(position: Int)
}

@Deprecated("don'tuse")
class SportViewModelImpl(
    private val getSportUseCase: GetSportUseCase,
    private val saveSportUseCase: SaveSportUseCase,
    private val router: Router
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