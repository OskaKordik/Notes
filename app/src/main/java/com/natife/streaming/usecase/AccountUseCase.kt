package com.natife.streaming.usecase

import com.natife.streaming.data.Profile
import com.natife.streaming.mock.MockAccountRepository

interface AccountUseCase {
    suspend fun getProfile(): Profile
}

class AccountUseCaseImpl(private val accountRepository: MockAccountRepository) : AccountUseCase {
    override suspend fun getProfile(): Profile {
        return accountRepository.getProfile()
    }
}
