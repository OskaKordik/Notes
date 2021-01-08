package com.natife.streaming.usecase

interface SaveSportUseCase {
    suspend fun execute(sportId: Int)
}

class SaveSportUseCaseImpl : SaveSportUseCase {

    override suspend fun execute(sportId: Int) {
        //todo need repository
    }

}