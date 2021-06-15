package com.natife.streaming.ui.search.type

//abstract class TypeDialogViewModel: BaseViewModel() {
//    abstract fun select(position: Int)
//
//    abstract val list: LiveData<List<String>>
//}
//
//class TypeDialogViewModelImpl(
//    private val typeUseCase: SearchTypeUseCase,
//    private val searchPrefs: SearchPrefs,
//    private val router: Router)
//    : TypeDialogViewModel() {
//    override fun select(position: Int) {
//        searchPrefs.saveType(position)
//        router.navigateUp()
//    }
//
//    override val list = MutableLiveData<List<String>>()
//
//    init {
//        launch {
//            list.value = typeUseCase.execute()
//        }
//    }
//}