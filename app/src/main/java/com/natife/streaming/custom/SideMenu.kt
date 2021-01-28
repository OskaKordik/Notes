package com.natife.streaming.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.navigation.NavController
import com.natife.streaming.R
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
) : FrameLayout(context, attrs, defStyleAttr), KoinComponent {

    private var router: Router? = null
    private val authPrefs: AuthPrefs by inject()

    private val navListener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
        Timber.e("destination ${resources.getResourceName(destination.id)}")
       this.isVisible = when(destination.id ){
            R.id.loginFragment, R.id.matchProfileFragment->false
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

        }
        menuHome.setOnClickListener {
            router?.toHome()
        }
        menuFavorites.setOnClickListener {
            router?.navigate(R.id.action_main_tournamentFragment)
        }
        menuSettings.setOnClickListener {

        }

    }
    fun setProfile(name :String){
        accountText.text = name
    }
    fun setRouter(router: Router?){
        this.router = router
        this.router?.addListener(navListener)
    }
}