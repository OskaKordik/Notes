package com.natife.streaming.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.natife.streaming.db.LocalSqlDataSourse
import com.natife.streaming.db.entity.toPreferencesSport
import com.natife.streaming.usecase.GetSportUseCase
import com.natife.streaming.usecase.SaveSportUseCase
import org.koin.core.KoinComponent
import org.koin.core.get

class UpdateListOfSportsWorker(
    ctx: Context, params: WorkerParameters
) : CoroutineWorker(ctx, params), KoinComponent {
    private val getSportUseCase: GetSportUseCase = get()
    private val saveSportUseCase: SaveSportUseCase = get()
    private val localSqlDataSourse: LocalSqlDataSourse = get()

    override suspend fun doWork(): Result {

        //TODO надо сделать синхронизацию сервера с бд
        val sportsInDB = localSqlDataSourse.getPreferencesSport()
        if (sportsInDB.isEmpty()) {
            val loadSports = getSportUseCase.execute().toPreferencesSport()
            saveSportUseCase.savePreferencesSportList(loadSports)
        }
        return Result.success()
    }
}