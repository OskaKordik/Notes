package com.natife.streaming

import android.content.Context
import com.google.gson.GsonBuilder
import com.natife.streaming.base.EmptyViewModel
import com.natife.streaming.datasource.MatchDataSource
import com.natife.streaming.datasource.MatchDataSourceFactory
import com.natife.streaming.mock.MockAccountRepository
import com.natife.streaming.mock.MockLoginRepository
import com.natife.streaming.mock.MockMatchRepository
import com.natife.streaming.mock.MockSportRepository
import com.natife.streaming.preferenses.AuthPrefsImpl
import com.natife.streaming.preferenses.AuthPrefs
import com.natife.streaming.router.Router
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.natife.streaming.api.MainApi
import com.natife.streaming.api.interceptor.AuthInterceptor
import com.natife.streaming.api.interceptor.ErrorInterceptor
import com.natife.streaming.preferenses.SettingsPrefs
import com.natife.streaming.preferenses.SettingsPrefsImpl
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
import com.natife.streaming.ui.home.tournament.*
import com.natife.streaming.ui.login.LoginViewModel
import com.natife.streaming.ui.login.LoginViewModelImpl
import com.natife.streaming.ui.main.MainViewModel
import com.natife.streaming.ui.tournament.TournamentViewModel
import com.natife.streaming.usecase.*
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val viewModelModule = module {
    viewModel { EmptyViewModel() }
    viewModel<LoginViewModel> { LoginViewModelImpl(get(), get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel<AccountViewModel> { AccountViewModelImpl(get(), get(), get()) }
    viewModel { TournamentViewModel(get(), get()) }
    viewModel<HomeViewModel> { HomeViewModelImpl(get(), get(),get()) }
    viewModel<ScoreViewModel> { ScoreViewModelImpl(get(), get(), get()) }
    viewModel<SportViewModel> { SportViewModelImpl(get(), get(), get()) }
    viewModel<LiveViewModel> { LiveViewModelImpl(get(), get(), get()) }
    viewModel<TournamentDialogViewModel> { (args: TournamentDialogArgs) ->
        TournamentDialogViewModelImpl(
            args.thournuments,
            get(),
            get(),
            get(),
            get()
        )
    }
}

val prefsModule = module {
    single(named(PREFS_AUTH_QUALIFIER)) {
        androidContext().getSharedPreferences(
            PREFS_AUTH_NAME,
            Context.MODE_PRIVATE
        )
    }
    single(named(PREFS_SETTINGS_QUALIFIER)) {
        androidContext().getSharedPreferences(
            PREFS_SETTINGS_NAME,
            Context.MODE_PRIVATE
        )
    }
    single { AuthPrefsImpl(get(named(PREFS_AUTH_QUALIFIER))) as AuthPrefs }
    single { SettingsPrefsImpl(get(named(PREFS_SETTINGS_QUALIFIER))) as SettingsPrefs }
}

val useCaseModule = module {
    factory<LoginUseCase> { LoginUseCaseImpl(get(), get()) }
    factory<LogoutUseCase> { LogoutUseCaseImpl(get(), get()) }
    factory<AccountUseCase> { AccountUseCaseImpl(get(), get()) }
    factory<MatchUseCase> { MatchUseCaseImpl(get(),get()) }
    factory<GetShowScoreUseCase> { GetShowScoreUseCaseImpl() }
    factory<SaveShowScoreUseCase> { SaveShowScoreUseCaseImpl(get()) }
    factory<GetSportUseCase> { GetSportUseCaseImpl(get()) }
    factory<SaveSportUseCase> { SaveSportUseCaseImpl(get()) }
    factory<GetLiveUseCase> { GetLiveUseCaseImpl() }
    factory<SaveLiveUseCase> { SaveLiveUseCaseImpl(get()) }
    factory <GetTournamentUseCase>{ GetTournamentUseCaseImpl(get()) }
    factory <SaveTournamentUseCase>{ SaveTournamentUseCaseImpl(get()) }
    factory { TournamentUseCase() }
}

val mockModule = module {
    single { MockLoginRepository() }
    single { MockAccountRepository() }
    single { MockMatchRepository() }
    single { MockSportRepository() }
}

val routerModule = module {
    single { Router() }
}

val dataSourceModule = module {
    factory { MatchDataSourceFactory(get()) }
}

val apiModule = module {
    factory { GsonConverterFactory.create(GsonBuilder().setLenient().create()) }

    factory { AuthInterceptor(get(),get()) }
    factory { ErrorInterceptor() }

    factory {
        LoggingInterceptor.Builder()
            .setLevel(Level.BASIC)
            .log(Platform.INFO)
            .tag("MyRequests")
            .build()
    }

    factory(named(MAIN_API_CLIENT_QUALIFIER)) {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .addInterceptor(get<ErrorInterceptor>())
            .addInterceptor(get<LoggingInterceptor>())
            .build()
    }

    single(named(MAIN_API_QUALIFIER)) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(get<GsonConverterFactory>())
            .client(get(named(MAIN_API_CLIENT_QUALIFIER)))
            .build()
    }

    single { get<Retrofit>(named(MAIN_API_QUALIFIER)).create(MainApi::class.java) }
}

val appModules = arrayListOf(
    viewModelModule,
    prefsModule,
    useCaseModule,
    mockModule,
    dataSourceModule,
    routerModule,
    apiModule
)