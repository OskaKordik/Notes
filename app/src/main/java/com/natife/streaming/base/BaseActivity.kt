package com.natife.streaming.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.natife.streaming.ext.showToast
import com.natife.streaming.ext.subscribe
import com.natife.streaming.router.Router
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

    var router: Router? = null

    protected lateinit var viewModel: VM


    abstract fun getLayoutRes(): Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        getLayoutRes()?.also {
            setContentView(it)
        }

        viewModel = getViewModel(clazz = getViewModelKClass())
        router = onCreateRouter()
        viewModel.router = router


        subscribe(viewModel.defaultErrorLiveData) {
            onError(it)
        }
    }

    abstract fun onCreateRouter(): Router?

    protected open fun onError(throwable: Throwable) {
        throwable.localizedMessage?.let {
            showToast(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getViewModelKClass(): KClass<VM> {
        val actualClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
        return actualClass.kotlin
    }


}