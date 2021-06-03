package com.natife.streaming.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.natife.streaming.db.LocalSqlDataSourse
import com.natife.streaming.db.entity.toPreferencesTournament
import com.natife.streaming.usecase.GetTournamentUseCase
import com.natife.streaming.usecase.SaveTournamentUseCase
import org.koin.core.KoinComponent
import org.koin.core.get

class UpdateListOfTournamentWorker(
    ctx: Context, params: WorkerParameters
) : CoroutineWorker(ctx, params), KoinComponent {
    private val tournamentUseCase: GetTournamentUseCase = get()
    private val saveTournamentUseCase: SaveTournamentUseCase = get()
    private val localSqlDataSourse: LocalSqlDataSourse = get()

    override suspend fun doWork(): Result {

        //TODO надо сделать синхронизацию сервера с бд
        val preferencesTournamentInDB = localSqlDataSourse.getPreferencesTournament()
        if (preferencesTournamentInDB.isEmpty()) {
            val preferencesTournament = tournamentUseCase.execute().toPreferencesTournament()
            saveTournamentUseCase.saveTournamentList(preferencesTournament)
        }
        return Result.success()
    }
}