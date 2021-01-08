package com.natife.streaming

import android.content.Context
import com.natife.streaming.base.EmptyViewModel
import com.natife.streaming.datasource.MatchDataSource
import com.natife.streaming.datasource.MatchDataSourceFactory
import com.natife.streaming.mock.MockAccountRepository
import com.natife.streaming.mock.MockLoginRepository
import com.natife.streaming.mock.MockMatchRepository
import com.natife.streaming.mock.MockVideosRepository
import com.natife.streaming.preferenses.AuthPrefsImpl
import com.natife.streaming.preferenses.AuthPrefs
import com.natife.streaming.ui.account.AccountViewModel
import com.natife.streaming.ui.account.AccountViewModelImpl
import com.natife.streaming.ui.home.HomeViewModel
import com.natife.streaming.ui.home.HomeViewModelImpl
import com.natife.streaming.ui.login.LoginViewModel
import com.natife.streaming.ui.login.LoginViewModelImpl
import com.natife.streaming.ui.main.MainViewModel
import com.natife.streaming.ui.main.MainViewModelImpl
import com.natife.streaming.ui.player.PlayerViewModel
import com.natife.streaming.ui.player.PlayerViewModelImpl
import com.natife.streaming.usecase.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { EmptyViewModel() }
    viewModel<LoginViewModel> { LoginViewModelImpl(get()) }
    viewModel<MainViewModel> { MainViewModelImpl() }
    viewModel<HomeViewModel> { HomeViewModelImpl(get()) }
    viewModel<AccountViewModel> { AccountViewModelImpl(get(), get()) }
    viewModel <PlayerViewModel>{ PlayerViewModelImpl(get(), get(), get())}
}

val prefsModule = module {
    single(named(PREFS_AUTH_QUALIFIER)) {
        androidContext().getSharedPreferences(
            PREFS_AUTH_NAME,
            Context.MODE_PRIVATE
        )
    }
    single { AuthPrefsImpl(get(named(PREFS_AUTH_QUALIFIER))) as AuthPrefs }
}

val useCaseModule = module {
    factory<LoginUseCase> { LoginUseCaseImpl(get(), get()) }
    factory<LogoutUseCase> { LogoutUseCaseImpl(get(), get()) }
    factory<AccountUseCase> { AccountUseCaseImpl(get()) }
    factory<MatchUseCase> { MatchUseCaseImpl(get()) }
    factory<GetVideosUseCase> { GetVideosUseCaseImpl(get())}
    factory<GetMatchInfoUseCase> { GetMatchInfoUseCaseImpl(get())}
}

val mockModule = module {
    single { MockLoginRepository() }
    single { MockAccountRepository() }
    single { MockMatchRepository() }
    single { MockVideosRepository() }
}

val dataSourceModule = module{
    factory { MatchDataSourceFactory(get()) }
}
val appModules = arrayListOf(viewModelModule, prefsModule, useCaseModule, mockModule, dataSourceModule)