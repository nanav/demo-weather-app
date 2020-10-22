package com.nanav.weather.ext.android

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment

fun Activity.color(@ColorRes color: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(color, theme)
    } else {
        @Suppress("DEPRECATION")
        resources.getColor(color)
    }
}

fun Fragment.color(@ColorRes color: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(color, activity?.theme)
    } else {
        @Suppress("DEPRECATION")
        resources.getColor(color)
    }
}

fun View.color(@ColorRes color: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(color, context.theme)
    } else {
        @Suppress("DEPRECATION")
        resources.getColor(color)
    }
}

fun Activity.dimen(@DimenRes dimen: Int): Int {
    return resources.getDimensionPixelSize(dimen)
}

fun Fragment.dimen(@DimenRes dimen: Int): Int {
    return resources.getDimensionPixelSize(dimen)
}

fun View.dimen(@DimenRes dimen: Int): Int {
    return resources.getDimensionPixelSize(dimen)
}
