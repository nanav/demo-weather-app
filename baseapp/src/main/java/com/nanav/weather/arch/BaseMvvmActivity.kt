package com.nanav.weather.arch

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.nanav.weather.ext.android.viewBinding
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

abstract class BaseMvvmActivity<T : BaseViewModel, B: ViewBinding>(
    viewModelClass: KClass<T>,
    bindingInflater: (LayoutInflater) -> B
) : AppCompatActivity(), Launcher {

    /*
    * DO NOT CALL BEFORE ONCREATE
    * */
    protected val viewModel : T by stateViewModel(viewModelClass)

    protected val layout by viewBinding(bindingInflater)

    private val disposableView = DisposablesForLifecycle().apply { lifecycle.addObserver(this) }
    private val resultLambdas = mutableMapOf<Int, ActivityResultCallback?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        disposableView.onBeforeCreate()
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        disposableView.onBeforeStart()
        super.onStart()
    }

    override fun onResume() {
        disposableView.onBeforeResume()
        super.onResume()
    }

    fun Disposable.disposeOnPause() {
        this.addTo(disposableView.onPauseDisposable)
    }

    fun Disposable.disposeOnStop() {
        this.addTo(disposableView.onStopDisposable)
    }

    fun Disposable.disposeOnDestroy() {
        this.addTo(disposableView.onDestroyDisposable)
    }

    override fun context() = this

    override fun launch(intent: Intent) {
        startActivity(intent)
    }

    override fun launchForResult(intent: Intent, code: Int, onResult: ActivityResultCallback?) {
        resultLambdas[code] = onResult
        startActivityForResult(intent, code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        resultLambdas[requestCode]?.let {
            it(resultCode, data)
        }
    }

    fun setTopView(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            v.setPadding(0, insets.systemWindowInsetTop, 0, 0)
            insets.consumeSystemWindowInsets()
        }
    }

    fun setDarkStatusBarIcons() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val flags = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.decorView.systemUiVisibility = flags
        }
    }

    fun setDrawBehindStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    protected fun <T> LiveData<T>.observe(callback: (T) -> Unit) {
        this.observe(this@BaseMvvmActivity, Observer { callback(it) })
    }
}
