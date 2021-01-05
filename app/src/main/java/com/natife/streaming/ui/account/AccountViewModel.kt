package com.natife.streaming.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.usecase.AccountUseCase
import com.natife.streaming.usecase.LogoutUseCase

abstract class AccountViewModel : BaseViewModel() {
    abstract fun logout()
    abstract fun back()

    abstract val nameLiveData: LiveData<String>
    abstract val emailLiveData: LiveData<String>
}

class AccountViewModelImpl(
    private val logoutUseCase: LogoutUseCase,
    private val accountUseCase: AccountUseCase
) : AccountViewModel() {
    override val nameLiveData = MutableLiveData<String>()
    override val emailLiveData = MutableLiveData<String>()

    init {
        launch {
            val profile = accountUseCase.getProfile()
            nameLiveData.value = "${profile.firstName} ${profile.lastName}"
            emailLiveData.value = profile.email
        }
    }

    override fun logout() {
        launch {
            logoutUseCase.execute()
        }
    }

    override fun back() {
        router?.navigateUp()
    }


}