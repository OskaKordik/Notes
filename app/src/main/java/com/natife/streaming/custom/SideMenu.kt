package com.natife.streaming.custom

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.leanback.widget.BrowseFrameLayout
import androidx.navigation.NavController
import com.natife.streaming.R
import com.natife.streaming.data.match.Match
import com.natife.streaming.preferenses.AuthPrefs
import com.natife.streaming.router.Router
import kotlinx.android.synthetic.main.view_side_menu.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

class SideMenu @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BrowseFrameLayout(context, attrs, defStyleAttr), KoinComponent {

    private var router: Router? = null
    private val authPrefs: AuthPrefs by inject()
    var activity: Activity?= null
    var prefered :(()->Unit)? = null

    private val navListener = NavController.OnDestinationChangedListener { controller, destination, arguments ->

       this.isVisible = when(destination.id ){
            R.id.loginFragment, R.id.matchProfileFragment, R.id.matchSettingsFragment, R.id.watchFragment, R.id.playerFragment->false

           else -> true
       }
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_side_menu, this, false)
        this.addView(view)
        authPrefs.getProfile()?.let {
            accountText.text = it.firstName+" "+it.lastName
        }


        GlobalScope.launch {
            authPrefs.getProfileFlow().collect {
                it?.let {
                    accountText.text = it.firstName+" "+it.lastName
                }
            }
        }

        menuAccount.setOnClickListener {
            router?.toAccount()
        }
        menuSearch.setOnClickListener {
            router?.toSearch()
        }
        menuHome.setOnClickListener {
            router?.toHome()
        }
        menuFavorites.setOnClickListener {
            router?.toFavorites()
           //router?.navigate(R.id.action_main_tournamentFragment)
        }
        menuSettings.setOnClickListener {
            router?.toSettings()
        }

    }
    fun setProfile(name :String){
        accountText.text = name
    }
    fun setRouter(router: Router?){
        this.router = router
        this.router?.addListener(navListener)
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        Timber.e(" onFocusChangeListener $gainFocus")
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
    }


}