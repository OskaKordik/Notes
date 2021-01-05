package com.natife.streaming.usecase

import com.natife.streaming.data.Profile
import com.natife.streaming.mock.MockAccountRepository


/**
 * Выступает в роли интерфейса между ViewModel и Api.
 * Необходимо реализовать свой UseCaseImpl или отредактировать имеющийся
 * в нем же можно мапить данные.
 */
interface AccountUseCase {
    suspend fun getProfile(): Profile
}

class AccountUseCaseImpl(private val accountRepository: MockAccountRepository) : AccountUseCase {
    override suspend fun getProfile(): Profile {
        return accountRepository.getProfile()
    }
}
