package com.carelon.whe.util

import android.util.Log
import com.carelon.whe.BuildConfig

object Logger {

    fun logD(tag: String = "WhxApplication", message: String) {
        if (BuildConfig.DEBUG)
            Log.d(tag, message)
    }

    fun logV(tag: String = "WhxApplication", message: String) {
        if (BuildConfig.DEBUG)
            Log.v(tag, message)
    }

    fun logE(tag: String = "WhxApplication", message: String) {
        if (BuildConfig.DEBUG)
            Log.d(tag, message)
    }

    fun logW(tag: String = "WhxApplication", message: String) {
        if (BuildConfig.DEBUG)
            Log.d(tag, message)
    }

}