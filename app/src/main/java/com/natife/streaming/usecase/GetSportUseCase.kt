package com.natife.streaming.usecase

import com.natife.streaming.data.Sport
import com.natife.streaming.mock.MockSportRepository

interface GetSportUseCase{
    suspend fun execute(): List<Sport>
}

class GetSportUseCaseImpl(private val mockSportRepository: MockSportRepository): GetSportUseCase{
    override suspend fun execute(): List<Sport> {
        return mockSportRepository.getSportList()
    }

}