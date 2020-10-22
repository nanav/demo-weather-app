package com.nanav.weather.ext.android

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView

fun Context.getCompatColor(@ColorRes colorId: Int): Int {
    return ResourcesCompat.getColor(resources, colorId, null)
}

inline fun RecyclerView.ViewHolder.getColor(@ColorRes colorId: Int): Int {
    return itemView.context.getCompatColor(colorId)
}
