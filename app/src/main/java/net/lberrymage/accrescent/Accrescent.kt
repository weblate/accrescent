package net.lberrymage.accrescent

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import net.lberrymage.accrescent.workers.RefreshDevelopersWorker
import java.time.Duration
import javax.inject.Inject

@HiltAndroidApp
class Accrescent : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "REFRESH_DEVELOPERS",
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<RefreshDevelopersWorker>(Duration.ofHours(4)).build()
        )
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder().setWorkerFactory(workerFactory).build()
}
