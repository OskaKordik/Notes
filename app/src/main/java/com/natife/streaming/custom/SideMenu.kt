package com.natife.streaming.custom

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.natife.streaming.R
import com.natife.streaming.router.Router
import kotlinx.android.synthetic.main.view_side_menu.view.*
import timber.log.Timber

class SideMenu @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr){

    private var router: Router? = null
    private val navListener = NavController.OnDestinationChangedListener { controller, destination, arguments ->

    }

    init {
     val view =  LayoutInflater.from(context).inflate(R.layout.view_side_menu,this,false)
        this.addView(view)

        menuAccount.setOnClickListener {
            router?.toAccount()
        }
        menuSearch.setOnClickListener {

        }
        menuHome.setOnClickListener {
            router?.toHome()
        }
        menuFavorites.setOnClickListener {
            //TODO remove mocked logic
            router?.toFavorites()
        }
        menuSettings.setOnClickListener {

        }

    }

    fun setRouter(router: Router?){
        this.router = router
    }

    override fun requestChildFocus(child: View?, focused: View?) {
        Timber.d("kokodkdk$ request")
        super.requestChildFocus(child, focused)
    }

    override fun findFocus(): View {
        val view = super.findFocus()
        Timber.d("kokodkdk$ find $view ${hasFocus()}")
        return view
    }

    override fun clearChildFocus(child: View?) {
        Timber.d("kokodkdk$ child ")
        super.clearChildFocus(child)
    }

    override fun focusSearch(focused: View?, direction: Int): View {
        val view = super.focusSearch(focused, direction)
        Timber.d("kokodkdk$ search $view")


        return view
    }


}