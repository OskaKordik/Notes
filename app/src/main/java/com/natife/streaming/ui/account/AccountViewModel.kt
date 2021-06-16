package com.natife.streaming.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.AccountUseCase
import com.natife.streaming.usecase.LogoutUseCase
import com.natife.streaming.data.Profile

abstract class AccountViewModel : BaseViewModel() {
    abstract fun logout()
    abstract fun back()

    abstract val nameLiveData: LiveData<String>
    abstract val emailLiveData: LiveData<String>
    abstract val profileLiveData: LiveData<Profile>
}

class AccountViewModelImpl(
    private val router: Router,
    private val logoutUseCase: LogoutUseCase,
    private val accountUseCase: AccountUseCase
) : AccountViewModel() {
    override val nameLiveData = MutableLiveData<String>()
    override val emailLiveData = MutableLiveData<String>()
    override val profileLiveData = MutableLiveData<Profile>()

    init {
        launch {
            val profile = accountUseCase.getProfile()
            nameLiveData.value = "${profile.firstName} ${profile.lastName}"
            emailLiveData.value = profile.email
            profileLiveData.value = profile
        }
    }

    override fun logout() {
        launch {
            logoutUseCase.execute(true)
            router.navigate(R.id.action_global_nav_auth)
        }
    }

    override fun back() {
        router.navigateUp()
    }
}