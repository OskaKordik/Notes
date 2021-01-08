package com.natife.streaming.usecase

interface SaveShowScoreUseCase {
    suspend fun execute(yes:Boolean)
}
class SaveShowScoreUseCaseImpl: SaveShowScoreUseCase{
    override suspend fun execute(yes: Boolean) {

    }
}