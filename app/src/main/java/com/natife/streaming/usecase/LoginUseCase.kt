package com.natife.streaming.usecase

import com.google.gson.Gson
import com.natife.streaming.API_LOGIN
import com.natife.streaming.api.MainApi
import com.natife.streaming.api.exception.ApiException
import com.natife.streaming.data.dto.LoginDTO
import com.natife.streaming.data.request.BaseRequest
import com.natife.streaming.data.request.RequestLogin
import com.natife.streaming.mock.MockLoginRepository
import com.natife.streaming.preferenses.AuthPrefs
import retrofit2.HttpException
import timber.log.Timber
import java.lang.Exception

/**
 * Выступает в роли интерфейса между ViewModel и Api.
 * Необходимо реализовать свой UseCaseImpl или отредактировать имеющийся
 * в нем же можно мапить данные.
 */
interface LoginUseCase {
    suspend fun execute(
        email: String,
        password: String,
        onComplete: (com.natife.streaming.utils.Result<String>) -> Unit
    )
}

class LoginUseCaseImpl(
    private val api: MainApi,
    private val authPrefs: AuthPrefs
) : LoginUseCase {
    override suspend fun execute(
        email: String,
        password: String,
        onComplete: (com.natife.streaming.utils.Result<String>) -> Unit
    ) {

        val request = BaseRequest(
            procedure = API_LOGIN,
            params = RequestLogin(
                email,
                password
            )
        )
        Timber.e("jkjdfkjf jodijoijdofi")
        try {
            val login = api.login(request)
            Timber.e("jkjdfkjf $login")
            if (login.status == 1) {
                onComplete(com.natife.streaming.utils.Result.success("Success"))
            }
        }catch (e: ApiException){
            Timber.e("jkjdfkjf ${e.body}")
            try {
                val body = Gson().fromJson(e.body,LoginDTO::class.java)
                Timber.e("jkjdfkjf ${body}")
                when (body.status) {
                    2 -> onComplete(com.natife.streaming.utils.Result.error<String>("Email не найден"))
                    3 -> onComplete(com.natife.streaming.utils.Result.error<String>("Неверный email или пароль"))
                    4 -> onComplete(com.natife.streaming.utils.Result.error<String>("Аккаунт удален или заблокирован"))
                    5 -> onComplete(com.natife.streaming.utils.Result.error<String>("Аккаунт истек"))
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }


    }
}

