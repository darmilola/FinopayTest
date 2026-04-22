package com.nrs.finopaytest.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.nrs.finopaytest.data.local.WeatherDao
import com.nrs.finopaytest.data.mapper.toWeather
import com.nrs.finopaytest.domain.repository.WeatherRepository
import com.nrs.finopaytest.notifications.WeatherNotificationManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class WeatherSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: WeatherRepository,
    private val dao: WeatherDao,
    private val notificationManager: WeatherNotificationManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            repository.refreshWeather()
            
            // Get a favorite city to notify about
            dao.getAnyFavoriteCity()?.let { favorite ->
                notificationManager.showWeatherNotification(favorite.toWeather())
            }
            
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }

    companion object {
        private const val WORK_NAME = "WeatherSyncWorker"

        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val repeatingRequest = PeriodicWorkRequestBuilder<WeatherSyncWorker>(1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest
            )
        }
    }
}
