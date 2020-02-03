package com.omermuhammed.omdbsearch.utils

import android.util.Log
//import com.crashlytics.android.Crashlytics
import timber.log.Timber

// Timber class for Crashlytics reporting in release builds
class CrashReportingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        if (priority == Log.ERROR || priority == Log.DEBUG) {
//            Crashlytics.log(priority, tag, message)
            if (throwable != null) {
//                Crashlytics.logException(throwable)
            }
        } else return
    }
}