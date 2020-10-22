package com.nanav.weather.arch

import android.content.Context
import android.content.Intent

typealias ActivityResultCallback = (resultCode: Int, data: Intent?) -> Unit

/**
 * The launcher system is used as both activities and fragments open
 * other activities in the app, but using this it allows only one start
 * method to be written for activities
 */
interface Launcher {
    fun context(): Context
    fun launch(intent: Intent)
    fun launchForResult(intent: Intent, code: Int, onResult: ActivityResultCallback? = null)
}
