package com.natife.streaming.ui.login

import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.LoginUseCase
import com.natife.streaming.utils.Result

abstract class LoginViewModel : BaseViewModel() {

    abstract fun login(email: String, password: String, onError: ((String?) -> Unit))
}

class LoginViewModelImpl(
    private val router: Router,
    private val loginUseCase: LoginUseCase
) : LoginViewModel() {

    override fun login(email: String, password: String, onError: ((String?) -> Unit)) {
        launch {
            loginUseCase.execute(email, password) { result ->
                if (result.status == Result.Status.SUCCESS) {
                    router.navigate(R.id.action_global_nav_main)
                } else {
                    onError.invoke(result.message)
                }
            }
        }
    }
}

