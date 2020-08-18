package com.nerdinfusions.statusx.commoners

import com.onesignal.OneSignal
import androidx.multidex.MultiDexApplication
import timber.log.Timber

class Application : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
		// OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        Timber.plant(Timber.DebugTree())
    }
}