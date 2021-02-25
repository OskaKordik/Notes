package com.natife.streaming.usecase

import com.natife.streaming.API_SEARCH
import com.natife.streaming.api.MainApi
import com.natife.streaming.data.request.BaseRequest
import com.natife.streaming.data.request.SearchRequest
import com.natife.streaming.data.search.SearchResult
import com.natife.streaming.db.dao.SearchDao
import com.natife.streaming.db.entity.SearchEntity
import com.natife.streaming.utils.ImageUrlBuilder
import timber.log.Timber

interface SearchUseCase {
    suspend fun execute(name: String, local: Boolean = false): List<SearchResult>
    suspend fun execute(result: SearchResult)
}

class SearchUseCaseImpl(private val api: MainApi, private val dao: SearchDao) : SearchUseCase {
    override suspend fun execute(name: String, local: Boolean): List<SearchResult> {
        val results = mutableListOf<SearchResult>()

        if (local){
            val fromDb = dao.getResults("%$name%")
            results.addAll(fromDb.map {
                SearchResult(
                id=it.id,
                gender = it.gender,
                image = it.image,
                placeholder = it.placeholder,
                type = SearchResult.Type.valueOf(it.type),
                sport = it.sport,
                name =it.name
            ) })
        Timber.e("JNOXNONS $fromDb")
        }else{
            val response = api.search(
                BaseRequest(
                    procedure = API_SEARCH,
                    params = SearchRequest(name)
                )
            )
            response?.players?.let {
                results.addAll(it.map {
                    SearchResult(
                        id = it.id,
                        name = "${it.firstnameRus} ${it.lastnameRus}", // TODO multilang
                        type = SearchResult.Type.PLAYER,
                        image = ImageUrlBuilder.getUrl(
                            it.sport,
                            ImageUrlBuilder.Companion.Type.PLAYER,
                            it.id
                        ),
                        placeholder = ImageUrlBuilder.getPlaceholder(
                            it.sport,
                            ImageUrlBuilder.Companion.Type.PLAYER
                        ),
                        gender = it.gender,
                        sport = it.sport
                    )
                })
            }
            response?.teams?.let {
                results.addAll(it.map {
                    SearchResult(
                        id = it.id,
                        name = it.nameRus, // TODO multilang
                        type = SearchResult.Type.TEAM,
                        image = ImageUrlBuilder.getUrl(
                            it.sport,
                            ImageUrlBuilder.Companion.Type.TEAM,
                            it.id
                        ),
                        placeholder = ImageUrlBuilder.getPlaceholder(
                            it.sport,
                            ImageUrlBuilder.Companion.Type.TEAM
                        ),
                        gender = it.gender,
                        sport = it.sport

                    )
                })
            }
            response?.tournaments?.let {
                results.addAll(it.map {
                    SearchResult(
                        id = it.id,
                        name = it.nameRus, // TODO multilang
                        type = SearchResult.Type.TOURNAMENT,
                        image = ImageUrlBuilder.getUrl(
                            it.sport,
                            ImageUrlBuilder.Companion.Type.TOURNAMENT,
                            it.id
                        ),
                        placeholder = ImageUrlBuilder.getPlaceholder(
                            it.sport,
                            ImageUrlBuilder.Companion.Type.TOURNAMENT
                        ),
                        gender = it.gender,
                        sport = it.sport
                    )
                })
            }
        }


        return results
    }

    override suspend fun execute(result: SearchResult) {
        dao.insert(SearchEntity(
            id=result.id,
            gender = result.gender,
            image = result.image,
            placeholder = result.placeholder,
            type = result.type.name,
            sport = result.sport,
            name = result.name
        ))

    }
}