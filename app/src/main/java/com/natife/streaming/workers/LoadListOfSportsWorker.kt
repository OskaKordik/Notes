package com.natife.streaming.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.natife.streaming.db.entity.toPreferencesSport
import com.natife.streaming.usecase.GetSportUseCase
import com.natife.streaming.usecase.SaveSportUseCase
import org.koin.core.KoinComponent
import org.koin.core.get

class LoadListOfSportsWorker(
    ctx: Context, params: WorkerParameters
) : CoroutineWorker(ctx, params), KoinComponent {
    private val getSportUseCase: GetSportUseCase = get()
    private val saveSportUseCase: SaveSportUseCase = get()

    override suspend fun doWork(): Result {
        val sports = getSportUseCase.execute().toPreferencesSport()
        val allSportsInBD = getSportUseCase.getAllUserPreferencesInSport()
        if (allSportsInBD.isEmpty()) {// проверяем локальное хранилище
            saveSportUseCase.savePreferencesSportList(sports)
        } else {
            // обновляем
            saveSportUseCase.deleteAllPreferencesSportAndCreateAndSynchronize(
                newList = sports,
                oldList = allSportsInBD
            )
        }
        return Result.success()
    }
}