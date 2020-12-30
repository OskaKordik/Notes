package com.natife.streaming.router

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
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

        init {


            val navGraph = navController.navInflater.inflate(R.navigation.nav_home)
            val destination = /*if (authPrefs.isLoggedIn()) {
                R.id.homeFragment
            } else {*/
                R.id.loginFragment
            //}
            navGraph.startDestination = destination
            navController.graph = navGraph
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

    }

    fun toHome() {
        TODO("Not yet implemented")
    }


}
