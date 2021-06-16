package com.natife.streaming.ui.search.gender

//abstract class GenderViewModel: BaseViewModel() {
//    abstract fun select(position: Int)
//
//    abstract val list: LiveData<List<String>>
//}
//class GenderViewModelImpl(private val genderUseCase: GenderUseCase,private val searchPrefs: SearchPrefs,private val router: Router): GenderViewModel() {
//    override fun select(position: Int) {
//        searchPrefs.saveGender(position+1)
//        router.navigateUp()
//    }
//
//    override val list = MutableLiveData<List<String>>()
//
//    init {
//        launch {
//            list.value = genderUseCase.execute()
//        }
//    }
//
//}