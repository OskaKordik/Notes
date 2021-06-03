package com.natife.streaming.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.natife.streaming.db.entity.toPreferencesTournament
import com.natife.streaming.usecase.GetTournamentUseCase
import com.natife.streaming.usecase.SaveTournamentUseCase
import org.koin.core.KoinComponent
import org.koin.core.get

class LoadListOfTournamentWorker(
    ctx: Context, params: WorkerParameters
) : CoroutineWorker(ctx, params), KoinComponent {
    private val tournamentUseCase: GetTournamentUseCase = get()
    private val saveTournamentUseCase: SaveTournamentUseCase = get()

    override suspend fun doWork(): Result {

        val preferencesTournament = tournamentUseCase.execute().toPreferencesTournament()
        saveTournamentUseCase.saveTournamentList(preferencesTournament)

        return Result.success()
    }
}