package com.natife.streaming.datasource

import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.PagingSource
import com.natife.streaming.data.match.Match
import com.natife.streaming.mock.MockMatchRepository
import timber.log.Timber

class MatchDataSourceFactory(private val mockMatchRepository: MockMatchRepository): InvalidatingPagingSourceFactory<Int, Match>(
    { MatchDataSource(mockMatchRepository) }){

    fun refresh(requestParams: MatchParams) {
        mockMatchRepository.prepare(requestParams)
         invalidate()
    }

}


class MatchDataSource(
    private val mockMatchRepository: MockMatchRepository
) : PagingSource<Int, Match>() {

    //TODO get only page info
    private var requestParams: MatchParams? = mockMatchRepository.requestParams


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Match> {

        Timber.e("load ${params.key}")
        try {
            val nextPage = params.key ?: 1

                val offset = nextPage - 1
                val matches = mockMatchRepository.getMatches(
                    date = requestParams!!.date,
                    limit = requestParams!!.pageSize,
                    offset = requestParams!!.pageSize * offset,
                    sportId = requestParams!!.sportId,
                    subOnly = requestParams!!.subOnly,
                    tournamentId = requestParams!!.tournamentId
                )
                Timber.e("load success")
                return LoadResult.Page(
                    data = matches,
                    prevKey = if (nextPage == 1) null else nextPage - 1,
                    nextKey = nextPage + 1
                )

        }catch (e: Exception){
            Timber.e("load error")
            e.printStackTrace()
            return LoadResult.Error(Throwable("Something went wrong"))
        }



    }

}

data class MatchParams(
    val date: String,
    val sportId: Int?,
    val subOnly: Boolean,
    val tournamentId: Int?,
    val pageSize: Int
)