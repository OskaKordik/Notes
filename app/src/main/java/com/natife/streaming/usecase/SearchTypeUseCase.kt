package com.natife.streaming.usecase

interface SearchTypeUseCase {
    suspend fun execute(): List<String>
}

class SearchTypeUseCaseImpl: SearchTypeUseCase {
    override suspend fun execute(): List<String> {
        return listOf("Команда","Игрок","Турнир")
    }
}