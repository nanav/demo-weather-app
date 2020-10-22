package com.nanav.weather.ui

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding

interface ViewBindingHolder<T : ViewBinding> {

    val layout: T?

    fun initBinding(binding: T, fragment: Fragment, onBound: (T.() -> Unit)? = null): View

    fun requireLayout(block: (T.() -> Unit)? = null): T
}

class ViewBindingHolderImpl<T : ViewBinding> : ViewBindingHolder<T>, LifecycleObserver {

    override var layout: T? = null

    private lateinit var lifecycle: Lifecycle
    private lateinit var fragmentName: String

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyView() {
        lifecycle.removeObserver(this)
        layout = null
    }

    override fun requireLayout(block: (T.() -> Unit)?) =
        layout?.apply { block?.invoke(this) } ?: throw IllegalStateException("Accessing binding views outside of Fragment lifecycle: $fragmentName")

    override fun initBinding(binding: T, fragment: Fragment, onBound: (T.() -> Unit)?): View {
        this.layout = binding
        lifecycle = fragment.viewLifecycleOwner.lifecycle
        lifecycle.addObserver(this)
        fragmentName = fragment::class.java.simpleName
        onBound?.invoke(binding)
        return binding.root
    }
}
