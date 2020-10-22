package com.nanav.weather.arch

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable

class DisposablesForLifecycle : LifecycleObserver {
    var onPauseDisposable = CompositeDisposable()
    var onStopDisposable = CompositeDisposable()
    var onDestroyDisposable = CompositeDisposable()

    fun onBeforeCreate() {
        if (onDestroyDisposable.isDisposed) {
            onDestroyDisposable = CompositeDisposable()
        }
    }

    fun onBeforeStart() {
        if (onStopDisposable.isDisposed) {
            onStopDisposable = CompositeDisposable()
        }
    }

    fun onBeforeResume() {
        if (onPauseDisposable.isDisposed) {
            onPauseDisposable = CompositeDisposable()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onAfterPause() {
        onPauseDisposable.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAfterStop() {
        onStopDisposable.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onAfterDestroy() {
        onDestroyDisposable.clear()
    }
}
