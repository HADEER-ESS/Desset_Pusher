package com.hadeer.dessetpusher

import android.app.Application
import timber.log.Timber

class DessertPucher : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}