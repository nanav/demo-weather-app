package com.nanav.weather.ext.android

import android.app.Activity
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

inline fun <T : ViewBinding> Activity.viewBinding(crossinline inflater: (LayoutInflater) -> T): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        val view = inflater.invoke(layoutInflater)
        setContentView(view.root)
        view
    }
}
