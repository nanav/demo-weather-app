package com.nanav.weather.arch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nanav.weather.arch.BaseMvvm
import io.reactivex.disposables.CompositeDisposable
import java.lang.IllegalStateException

abstract class BaseViewModel protected constructor(protected val savedStateHandle: SavedStateHandle? = null) : BaseMvvm.ViewModel, ViewModel()  {
    protected var subscription = CompositeDisposable()

    /**
     * Get a MutableLiveData from SavedStateHandle
     * Providing a blocking method to provide initialData
     *
     * Note that the initalData() method isn't called until the LiveData is accessed
     * and is only called if there's no saved data
     */
    protected fun <T, V: T> liveDataFactory(key: String, initialData: () -> V?): Lazy<MutableLiveData<T>> {
        if (savedStateHandle == null) throw IllegalStateException("Used live data factory but no savedStateHandle was provided")
        return lazy {
            val liveData: MutableLiveData<T> = savedStateHandle.getLiveData(key)
            if (liveData.value == null) {
                liveData.value = initialData()
            }
            liveData
        }
    }

    /**
     * Get a MutableLiveData from SavedStateHandle
     *
     * loadInitialData must set the LiveData value manually
     *
     * Note that the loadInitalData() method isn't called until the LiveData is accessed
     * and is only called if there's no saved data
     */
    protected fun <T, V: T> liveDataFactory(key: String, defaultValue: V? = null, loadInitialData: () -> Unit): Lazy<MutableLiveData<T>> {
        if (savedStateHandle == null) throw IllegalStateException("Used live data factory but no savedStateHandle was provided")
        return lazy {
            val liveData: MutableLiveData<T> = savedStateHandle.getLiveData(key)
            if (liveData.value == null) {
                liveData.value = defaultValue
                loadInitialData()
            }
            liveData
        }
    }

    override fun onCleared() {
        subscription.clear()
        super.onCleared()
    }
}
