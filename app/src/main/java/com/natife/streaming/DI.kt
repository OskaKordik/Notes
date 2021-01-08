package com.natife.streaming

import android.content.Context
import com.natife.streaming.base.EmptyViewModel
import com.natife.streaming.datasource.MatchDataSource
import com.natife.streaming.datasource.MatchDataSourceFactory
import com.natife.streaming.mock.MockAccountRepository
import com.natife.streaming.mock.MockLoginRepository
import com.natife.streaming.mock.MockMatchRepository
import com.natife.streaming.mock.MockSportRepository
import com.natife.streaming.preferenses.AuthPrefsImpl
import com.natife.streaming.preferenses.AuthPrefs
import com.natife.streaming.ui.account.AccountViewModel
import com.natife.streaming.ui.account.AccountViewModelImpl
import com.natife.streaming.ui.home.HomeViewModel
import com.natife.streaming.ui.home.HomeViewModelImpl
import com.natife.streaming.ui.home.live.LiveViewModel
import com.natife.streaming.ui.home.live.LiveViewModelImpl
import com.natife.streaming.ui.home.score.ScoreViewModel
import com.natife.streaming.ui.home.score.ScoreViewModelImpl
import com.natife.streaming.ui.home.sport.SportViewModel
import com.natife.streaming.ui.home.sport.SportViewModelImpl
import com.natife.streaming.ui.home.tournament.TournamentDialogArgs
import com.natife.streaming.ui.home.tournament.TournamentViewModel
import com.natife.streaming.ui.home.tournament.TournamentViewModelImpl
import com.natife.streaming.ui.login.LoginViewModel
import com.natife.streaming.ui.login.LoginViewModelImpl
import com.natife.streaming.ui.main.MainViewModel
import com.natife.streaming.ui.main.MainViewModelImpl
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
    viewModel<ScoreViewModel> { ScoreViewModelImpl(get(), get()) }
    viewModel<SportViewModel> { SportViewModelImpl(get(), get()) }
    viewModel<LiveViewModel> { LiveViewModelImpl(get(), get()) }
    viewModel<TournamentViewModel> { (args: TournamentDialogArgs)->TournamentViewModelImpl(args.thournuments) }
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
    factory<GetShowScoreUseCase> { GetShowScoreUseCaseImpl() }
    factory<SaveShowScoreUseCase> { SaveShowScoreUseCaseImpl() }
    factory<GetSportUseCase> { GetSportUseCaseImpl(get()) }
    factory<SaveSportUseCase> { SaveSportUseCaseImpl() }
    factory<GetLiveUseCase> { GetLiveUseCaseImpl() }
    factory<SaveLiveUseCase> { SaveLiveUseCaseImpl() }
}

val mockModule = module {
    single { MockLoginRepository() }
    single { MockAccountRepository() }
    single { MockMatchRepository() }
    single { MockSportRepository() }
}

val dataSourceModule = module{
    factory { MatchDataSourceFactory(get()) }
}
val appModules = arrayListOf(viewModelModule, prefsModule, useCaseModule, mockModule, dataSourceModule)