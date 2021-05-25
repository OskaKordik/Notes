package com.natife.streaming.ui.register

import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.AccountUseCase
import com.natife.streaming.usecase.LoginUseCase
import com.natife.streaming.utils.Result

abstract class RegisterViewModel : BaseViewModel() {
    abstract fun userRegistration(email: String, password: String, onError: ((String?) -> Unit))
}

class RegisterViewModelImpl(
    private val loginUseCase: LoginUseCase,
    private val accountUseCase: AccountUseCase,
    private val router: Router,
) : RegisterViewModel() {

    override fun userRegistration(email: String, password: String, onError: (String?) -> Unit) {
        launch {
            loginUseCase.execute(email, password) { result ->
                if (result.status == Result.Status.SUCCESS) {
                    launch {
                        accountUseCase.getProfile()
                        router.navigate(R.id.action_global_preferences)
                    }
                } else {
                    onError.invoke(result.message)
                }
            }
        }
    }
}