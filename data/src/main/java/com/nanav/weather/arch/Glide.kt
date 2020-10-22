package com.nanav.weather.arch

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.Excludes
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpLibraryGlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.nanav.weather.data.BuildConfig
import com.nanav.weather.data.R
import okhttp3.OkHttpClient
import org.koin.core.context.KoinContextHandler
import org.koin.core.qualifier.named
import java.io.InputStream

@GlideModule
@Excludes(OkHttpLibraryGlideModule::class)
class Glide : AppGlideModule() {

    override fun isManifestParsingEnabled() = false

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val memoryCacheSizeBytes = MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2f)
                .build()
                .memoryCacheSize
        val diskCacheSizeBytes = 1024 * 1024 * 100 // 100 MB

        builder.setDefaultRequestOptions(RequestOptions.errorOf(R.drawable.ic_error))

        builder.setMemoryCache(LruResourceCache(memoryCacheSizeBytes.toLong()))
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, "glide", diskCacheSizeBytes.toLong()))
        builder.setLogLevel(if (BuildConfig.DEBUG) Log.INFO else Log.ERROR)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(GlideUrl::class.java,
                InputStream::class.java,
                OkHttpUrlLoader.Factory(KoinContextHandler.get().get<OkHttpClient>()))
    }

    companion object {
        val CIRCLE = RequestOptions().circleCrop()
    }
}
