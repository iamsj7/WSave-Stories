package com.nerdinfusions.wsave.commoners

import androidx.multidex.MultiDexApplication
import timber.log.Timber

class Application : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}