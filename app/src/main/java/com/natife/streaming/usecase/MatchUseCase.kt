package com.natife.streaming.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.natife.streaming.data.match.Match
import com.natife.streaming.datasource.MatchDataSource
import com.natife.streaming.datasource.MatchDataSourceFactory
import com.natife.streaming.datasource.MatchParams
import com.natife.streaming.mock.MockMatchRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

interface MatchUseCase {
    suspend fun execute(
        date: String,
        sportId: Int? = null,
        subOnly: Boolean = true,
        tournamentId: Int? = null,
        pageSize: Int = 60
    )

    fun executeFlow(pageSize: Int = 60): Flow<PagingData<Match>>
}

class MatchUseCaseImpl(
    private val matchDataSourceFactory: MatchDataSourceFactory
) : MatchUseCase {

    override suspend fun execute(
        date: String,
        sportId: Int?,
        subOnly: Boolean,
        tournamentId: Int?,
        pageSize: Int
    ) {
        matchDataSourceFactory.refresh(
            MatchParams(
                date,
                sportId,
                subOnly,
                tournamentId,
                pageSize
            )
        )
        Timber.e("prepared")
    }

    override fun executeFlow(pageSize: Int): Flow<PagingData<Match>> {
        return Pager(PagingConfig(pageSize = pageSize)) {
            matchDataSourceFactory.invoke()
        }.flow
    }
}

