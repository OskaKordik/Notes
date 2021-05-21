package com.natife.streaming.usecase

import android.content.Context
import com.google.gson.Gson
import com.natife.streaming.API_LOGIN
import com.natife.streaming.API_TRANSLATE
import com.natife.streaming.R
import com.natife.streaming.api.MainApi
import com.natife.streaming.api.exception.ApiException
import com.natife.streaming.data.dto.LoginDTO
import com.natife.streaming.data.request.BaseRequest
import com.natife.streaming.data.request.RequestLogin
import com.natife.streaming.data.request.TranslateRequest
import com.natife.streaming.preferenses.AuthPrefs
import com.natife.streaming.preferenses.SettingsPrefs
import timber.log.Timber
import java.lang.Exception
import com.natife.streaming.utils.Result

/**
 * Выступает в роли интерфейса между ViewModel и Api.
 * Необходимо реализовать свой UseCaseImpl или отредактировать имеющийся
 * в нем же можно мапить данные.
 */
interface LoginUseCase {
    suspend fun execute(
        email: String,
        password: String,
        onComplete: (Result<String>) -> Unit
    )
}

class LoginUseCaseImpl(
    private val api: MainApi,
    private val authPrefs: AuthPrefs,
    private val settingsPrefs: SettingsPrefs,
    private val context: Context
) : LoginUseCase {
    override suspend fun execute(
        email: String,
        password: String,
        onComplete: (Result<String>) -> Unit
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
            Timber.e("jkjdfkjf before")
            val login = api.login(request)
            Timber.e("jkjdfkjf $login")
            if (login.status == 1) {
                onComplete(Result.success("Success"))
            }
        }catch (e: ApiException){
            Timber.e("jkjdfkjf ${e.body}")
            try {
                val body = Gson().fromJson(e.body,LoginDTO::class.java)
                Timber.e("jkjdfkjf ${body}")
                when (body.status) {
                    2 ,3 -> {
                        val answer = api.getTranslate(BaseRequest(procedure = API_TRANSLATE,params = TranslateRequest(language = settingsPrefs.getLanguage(),
                            listOf(context.resources.getInteger(R.integer.wrong_login_or_password)))))
                        onComplete(Result.error<String>(answer[context.resources.getInteger(R.integer.wrong_login_or_password).toString()]?.text))
                    }
                    4 ->{
                        val answer = api.getTranslate(BaseRequest(procedure = API_TRANSLATE,params = TranslateRequest(language = settingsPrefs.getLanguage(),
                            listOf(context.resources.getInteger(R.integer.email_is_blocked)))))
                        onComplete(Result.error<String>(answer[context.resources.getInteger(R.integer.email_is_blocked).toString()]?.text))
                    }
                    5 -> onComplete(Result.error<String>("Аккаунт истек"))
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }


    }
}

