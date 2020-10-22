package com.nanav.weather.data.managers

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.ReplaySubject
import timber.log.Timber

class RxConnectivity(context: Application) {
    private val subject = ReplaySubject.createWithSize<Boolean>(1)
    private val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val isConnected: Boolean
        get() {
            val activeNetwork = connectivityManager.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnected
        }

    val hasWifiConnectivity: Boolean
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            } else {
                val activeNetwork = connectivityManager.activeNetworkInfo
                activeNetwork != null && activeNetwork.type == ConnectivityManager.TYPE_WIFI
            }
        }
    
    init {

        val networkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET).build()

        connectivityManager.registerNetworkCallback(networkRequest, object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network?) {
                super.onLost(network)
                subject.onNext(isConnected)
            }

            override fun onAvailable(network: Network?) {
                super.onAvailable(network)
                subject.onNext(isConnected)
            }
        })

        subject.onNext(isConnected)

        watch().subscribe { connected -> Timber.d("User is connected: %s", connected) }
    }

    fun watch(): Flowable<Boolean> {
        return subject.share().toFlowable(BackpressureStrategy.LATEST)
    }

    fun <T> whenConnected(rxMethod: Single<T>): Single<T> {
        val load = watch()
                .filter { it }
                .firstOrError()
                .flatMap { rxMethod }

        return load.onErrorResumeNext(load)
    }
}
