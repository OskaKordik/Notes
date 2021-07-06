package com.natife.streaming.ui.matchprofile
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.natife.streaming.R
//import com.natife.streaming.base.BaseViewModel
//import com.natife.streaming.data.Video
//import com.natife.streaming.data.actions.Action
//import com.natife.streaming.data.match.Match
//import com.natife.streaming.preferenses.MatchSettingsPrefs
//import com.natife.streaming.router.Router
//import com.natife.streaming.usecase.ActionsUseCase
//import com.natife.streaming.utils.OneTimeScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import org.koin.core.KoinComponent
//import org.koin.core.inject
//import timber.log.Timber
//
//abstract class MatchSettingsViewModel: BaseViewModel() {
//    abstract fun select(action: Action)
//    abstract fun save()
//    abstract fun saveAllAfter(sec: Int)
//    abstract fun saveAllBefore(sec: Int)
//    abstract fun saveSelectedAfter(sec: Int)
//    abstract fun saveSelectedBefore(sec: Int)
//
//    abstract fun getAllAfter():Int
//    abstract fun getAllBefore():Int
//    abstract fun getSelectedAfter():Int
//    abstract fun getSelectedBefore():Int
//    abstract fun goToPrewatch()
//    abstract fun goBack()
//
//
//    abstract val actions: LiveData<List<Action>>
//
//}
//
//class MatchSettingsViewModelImpl(
//    private val match: Match,
//    private val sportId: Int,
//    private val videos: Array<Video>,
//    private val actionsUseCase: ActionsUseCase,
//    private val matchSettingsPrefs: MatchSettingsPrefs,
//    private val router: Router): MatchSettingsViewModel(), KoinComponent {
//
//    private val oneTimeScope: OneTimeScope by inject()
//    override fun select(action: Action) {
//        val _actions = actions.value?.toMutableList()
//        _actions?.find { it.id == action.id }?.let {
//            _actions.set(_actions.indexOf(it),action)
//             }
//        actions.value = _actions
//
//
//    }
//
//    override fun save() {
//        Timber.e("save ")
//        GlobalScope.launch {
//            Timber.e("save ${actions.value}")
//            actions.value?.let{
//                it.forEach {
//                    Timber.e("$it")
//                }
//                actionsUseCase.save(sportId,it)
//            }
//        }
//
//
//    }
//
//    override fun saveAllAfter(sec: Int) {
//        matchSettingsPrefs.saveAllSecAfter(sec)
//    }
//
//    override fun saveAllBefore(sec: Int) {
//        matchSettingsPrefs.saveAllSecBefore(sec)
//    }
//
//    override fun saveSelectedAfter(sec: Int) {
//        matchSettingsPrefs.saveSelectedSecAfter(sec)
//    }
//
//    override fun saveSelectedBefore(sec: Int) {
//        matchSettingsPrefs.saveSelectedSecBefore(sec)
//    }
//
//    override fun getAllAfter(): Int  = matchSettingsPrefs.getAllSecAfter()
//
//    override fun getAllBefore(): Int = matchSettingsPrefs.getAllSecBefore()
//
//    override fun getSelectedAfter(): Int = matchSettingsPrefs.getSelectedSecAfter()
//
//    override fun getSelectedBefore(): Int = matchSettingsPrefs.getSelectedSecBefore()
//    override fun goToPrewatch() {
//        router.navigate(MatchSettingsFragmentDirections.actionMatchSettingsFragmentToWatchFragment(match=match,video = videos))
//    }
//
//    override fun goBack() {
//        router.navigateUp()
//    }
//
//
//    override val actions = MutableLiveData<List<Action>>()
//
//
//    init {
//        launch {
//            withContext(Dispatchers.IO){
//                actions.postValue(actionsUseCase.execute(sportId))
//            }
//        }
//    }
//}