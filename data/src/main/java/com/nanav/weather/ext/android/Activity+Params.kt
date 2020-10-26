package com.nanav.weather.ext.android

import android.app.Activity

inline fun <reified T: Any> Activity.extra(key: String) = lazy {
    if (intent == null || intent.extras == null) {
        throw IllegalStateException("Intent/extras not set")
    }
    val value = intent!!.extras!!.get(key)
    value as? T ?: throw IllegalStateException("$key null or wrong type")
}
