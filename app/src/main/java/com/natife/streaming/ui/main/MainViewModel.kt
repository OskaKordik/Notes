package com.natife.streaming.ui.main

import androidx.lifecycle.MutableLiveData
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.preferenses.AuthPrefs
import com.natife.streaming.router.Router
import timber.log.Timber

class MainViewModel(
    private val authPrefs: AuthPrefs,
    private val router: Router
) : BaseViewModel() {
    val name = MutableLiveData<String>()
    fun back(){
        router.navigateUp()
    }
    init {
        launch {
            val isLoggedIn = authPrefs.isLoggedIn()
            Timber.e("IS LIGGED IN $isLoggedIn")
            if (isLoggedIn) {
                router.navigate(R.id.action_global_nav_main)
            } else {
                router.navigate(R.id.action_global_nav_auth)
            }
        }
        launch{
           collect(authPrefs.getProfileFlow()){

               name.value = "${it?.firstName?:""} ${it?.lastName?:""}"
           }
        }


    }
}