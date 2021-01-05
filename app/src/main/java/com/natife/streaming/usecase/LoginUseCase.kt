package com.natife.streaming.usecase

import com.natife.streaming.mock.MockLoginRepository
import com.natife.streaming.preferenses.AuthPrefs

/**
 * Выступает в роли интерфейса между ViewModel и Api.
 * Необходимо реализовать свой UseCaseImpl или отредактировать имеющийся
 * в нем же можно мапить данные.
 */
interface LoginUseCase {
    suspend fun execute(
        email: String,
        password: String,
        onComplete: (com.natife.streaming.utils.Result<Unit>) -> Unit
    )
}

class LoginUseCaseImpl(
    private val repository: MockLoginRepository,
    private val authPrefs: AuthPrefs
) : LoginUseCase {
    override suspend fun execute(
        email: String,
        password: String,
        onComplete: (com.natife.streaming.utils.Result<Unit>) -> Unit
    ) {
        val login = repository.login(email, password)
        if (login.status == 1) {
            authPrefs.saveAuthToken(login.token)
            onComplete(com.natife.streaming.utils.Result.success(Unit))
        } else {
            when (login.status) {
                2 -> com.natife.streaming.utils.Result.error<String>("Email не найден")
                3 -> com.natife.streaming.utils.Result.error<String>("Неверный email или пароль")
                4 -> com.natife.streaming.utils.Result.error<String>("Аккаунт удален или заблокирован")
                5 -> com.natife.streaming.utils.Result.error<String>("Аккаунт истек")
            }
        }
    }
}

