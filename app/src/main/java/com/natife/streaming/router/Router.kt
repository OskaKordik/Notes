package com.natife.streaming.router

import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.natife.streaming.R
import com.natife.streaming.ext.hideKeyboard
import org.koin.core.KoinComponent
import timber.log.Timber

/**
 * Примочка для Navigation component
 */
class Router : KoinComponent {

    private var activity: AppCompatActivity? = null
    @IdRes
    private var navHostId: Int? = null

    fun attach(activity: AppCompatActivity, @IdRes navHostId: Int) {
        this.activity = activity
        this.navHostId = navHostId
    }

    fun navigateUp() {
        val activity = activity ?: return
        val navHostId = navHostId ?: return
        val navController = activity.findNavController(navHostId)
        activity.hideKeyboard()
        navController.navigateUp()
    }

    fun navigate(@IdRes resId: Int) {
        Timber.e(" ${activity} $navHostId ")
        try {
            val activity = activity ?: return
            activity.hideKeyboard()
            val navHostId = navHostId ?: return
            val navController = activity.findNavController(navHostId)

            if (navController.currentDestination?.id != resId) {
                navController.navigate(resId)
            }
        } catch (exc: Exception) {
            Timber.e(exc)
        }
    }

    fun navigate(navDirections: NavDirections) {
        try {
            val activity = activity ?: return
            activity.hideKeyboard()
            val navHostId = navHostId ?: return
            val navController = activity.findNavController(navHostId)
            navController.navigate(navDirections)
        } catch (exc: Exception) {
            Timber.e(exc)
        }
    }

    fun toAccount() {
        navigate(R.id.action_main_accountFragment)
    }

    fun toHome() {

        navigate(R.id.action_main_homeFragment)
    }

    fun toLogin() {
        navigate(R.id.action_global_nav_auth)
    }

    fun toFavorites() {
        navigate(R.id.action_main_favoriteFragment)
    }

    fun toSettings() {
        navigate(R.id.action_main_settingsFragment)
    }
    fun toSearch() {
        navigate(R.id.action_main_searchFragment)
    }
    fun addListener(navListener: NavController.OnDestinationChangedListener) {
        val activity = activity ?: return
        val navHostId = navHostId ?: return
        val navController = activity.findNavController(navHostId)
        navController.addOnDestinationChangedListener(navListener)
    }

    fun toast(text: String) {
        val activity = activity ?: return
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }

    fun toast(@StringRes res: Int) {
        val activity = activity ?: return
        toast(activity.getString(res))
    }


}
