package com.natife.streaming.ui.login

import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.LoginUseCase
import com.natife.streaming.utils.Result
import timber.log.Timber

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
                Timber.e("jkjdfkjf ${result.status}")
                if (result.status == Result.Status.SUCCESS) {
                    Timber.e("jkjdfkjf !!!")
                    router.navigate(R.id.action_global_nav_main)
                } else {
                    onError.invoke(result.message)
                }
            }
        }
    }
}

