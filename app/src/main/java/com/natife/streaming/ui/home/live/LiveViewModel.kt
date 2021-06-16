package com.natife.streaming.ui.home.live

//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.natife.streaming.base.BaseViewModel
//import com.natife.streaming.data.LiveType
//import com.natife.streaming.router.Router
//import com.natife.streaming.usecase.GetLiveUseCase
//import com.natife.streaming.usecase.SaveLiveUseCase
//@Deprecated("don'tuse")
//abstract class LiveViewModel : BaseViewModel() {
//    abstract val list: LiveData<List<String>>
//    abstract fun saveType(selected: Int)
//    abstract fun onSelect(it: Int)
//}
//
//@Deprecated("don'tuse")
//class LiveViewModelImpl(
//    private val getLiveUseCase: GetLiveUseCase,
//    private val saveLiveUseCase: SaveLiveUseCase,
//    private val router: Router
//): LiveViewModel() {
//    override val list = MutableLiveData<List<String>>()
//    override fun saveType(selected: Int) {
//
//        saveLiveUseCase.execute(LiveType.values()[selected])
//    }
//
//    override fun onSelect(it: Int) {
//        when(it){
//            0-> saveLiveUseCase.execute(LiveType.LIVE)
//            1-> saveLiveUseCase.execute(LiveType.FINISHED)
//            2-> saveLiveUseCase.execute(LiveType.SOON)
//            3-> saveLiveUseCase.execute(LiveType.ALL)
//        }
//        router.navigateUp()
//
//    }
//
//    init {
//        launch {
//            val live = getLiveUseCase.execute()
//            list.value = live
//        }
//    }
//
//}