package com.natife.streaming.usecase

import com.natife.streaming.API_SPORTS
import com.natife.streaming.API_TRANSLATE
import com.natife.streaming.api.MainApi
import com.natife.streaming.data.Sport
import com.natife.streaming.data.request.BaseRequest
import com.natife.streaming.data.request.EmptyRequest
import com.natife.streaming.data.request.TranslateRequest
import com.natife.streaming.preferenses.SettingsPrefs

//new
interface GetSportUseCase {
    suspend fun execute(reload: Boolean = false): List<Sport>
}

class GetSportUseCaseImpl(private val api: MainApi, private val prefs: SettingsPrefs) :
    GetSportUseCase {

    private var catche: List<Sport>? = null

    override suspend fun execute(reload: Boolean): List<Sport> {
//        Timber.e("ijoijdoifj $catche $reload")

        if (catche == null || reload) {
//            Timber.e("ijoijdoifj dfjdjfpdijfpidjf")
            val sports = api.getSports(BaseRequest(procedure = API_SPORTS, params = EmptyRequest()))
            val translates =
                api.getTranslate(BaseRequest(procedure = API_TRANSLATE, params = TranslateRequest(
                    language = prefs.getLanguage(),
                    params = sports.map { it.lexic }
                )))
            catche = sports.map {
                Sport(id = it.id, translates[it.lexic.toString()]?.text ?: "", lexic = it.lexic)
            }
        }


        return catche!!
    }

}