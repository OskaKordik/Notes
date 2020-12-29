package com.natife.streaming

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {

}

val prefsModule = module{
    single(named(PREFS_AUTH_QUALIFIER)) {
        androidContext().getSharedPreferences(
            PREFS_AUTH_NAME,
            Context.MODE_PRIVATE
        )
    }
}

val useCaseModule = module {

}


val appModules = arrayListOf(viewModelModule, prefsModule, useCaseModule)