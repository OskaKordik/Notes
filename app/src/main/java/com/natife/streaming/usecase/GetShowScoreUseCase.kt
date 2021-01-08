package com.natife.streaming.usecase

interface GetShowScoreUseCase {
    suspend fun execute(): List<String>
}
class GetShowScoreUseCaseImpl: GetShowScoreUseCase{
    override suspend fun execute(): List<String> {
        //TODO need repository
        return listOf("Да","Нет")
    }

}