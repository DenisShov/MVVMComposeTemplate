package com.dshovhenia.mvvm.compose.template

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        setupInternalLogger()
    }

    private fun setupInternalLogger() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}
