package com.natife.streaming.router

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import com.natife.streaming.ui.main.MainActivity
import com.natife.streaming.R
import com.natife.streaming.preferenses.AuthPrefs
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

class Router(
    private val activity: MainActivity,
    @IdRes viewId: Int
) : KoinComponent {

    private val authPrefs by inject<AuthPrefs>()

    private val navController: NavController = activity.findNavController(viewId)
    private var navGraph: NavGraph? = null
    private var destination: Int? = null

    init {

        if (authPrefs.isLoggedIn()) {
            toHome()
        } else {
            toLogin()
        }

    }

    fun navigateUp() {
        navController.navigateUp()
    }

    fun navigate(@IdRes resId: Int) {
        try {
            if (navController.currentDestination?.id != resId) {
                navController.navigate(resId)
            }
        } catch (exc: Exception) {
            Timber.e(exc)
        }
    }

    fun navigate(navDirections: NavDirections) {
        try {
            navController.navigate(navDirections)
        } catch (exc: Exception) {
            Timber.e(exc)
        }
    }

    fun toAccount() {
        navGraph = navController.navInflater.inflate(R.navigation.nav_account)
        destination = R.id.accountFragment
        navController.graph = navGraph as NavGraph
    }

    fun toHome() {
        navGraph = navController.navInflater.inflate(R.navigation.nav_home)
        destination = R.id.homeFragment
        navController.graph = navGraph as NavGraph
    }

    fun toLogin() {
        navGraph = navController.navInflater.inflate(R.navigation.nav_login)
        destination = R.id.loginFragment
        navController.graph = navGraph as NavGraph
    }

    fun toFavorites() {

    }

    fun toSettings() {

    }
}
