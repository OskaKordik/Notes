package com.natife.streaming.mock

import com.natife.streaming.data.Login
import com.natife.streaming.data.Profile
import kotlinx.coroutines.delay

class MockAccountRepository {
    suspend fun getProfile(): Profile {
        delay(500)
        return Profile(firstName = "Иван",
        lastName = "Иванов",
        email = "konstantinopolsky@gmail.com")
    }
}