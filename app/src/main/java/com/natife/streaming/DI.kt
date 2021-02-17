package com.natife.streaming

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.natife.streaming.base.EmptyViewModel
import com.natife.streaming.datasource.MatchDataSource
import com.natife.streaming.datasource.MatchDataSourceFactory
import com.natife.streaming.mock.MockAccountRepository
import com.natife.streaming.mock.MockLoginRepository
import com.natife.streaming.mock.MockMatchRepository
import com.natife.streaming.mock.MockSportRepository
import com.natife.streaming.router.Router
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.natife.streaming.api.MainApi
import com.natife.streaming.api.interceptor.AuthInterceptor
import com.natife.streaming.api.interceptor.ErrorInterceptor
import com.natife.streaming.db.AppDatabase
import com.natife.streaming.preferenses.*
import com.natife.streaming.ui.account.AccountViewModel
import com.natife.streaming.ui.account.AccountViewModelImpl
import com.natife.streaming.ui.calendar.CalendarViewModel
import com.natife.streaming.ui.calendar.CalendarViewModelImpl
import com.natife.streaming.ui.favorites.FavoriteViewModel
import com.natife.streaming.ui.favorites.FavoriteViewModelImpl
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
import com.natife.streaming.ui.tournament.TournamentFragmentArgs
import com.natife.streaming.ui.matchprofile.*
import com.natife.streaming.ui.player.PlayerFragmentArgs
import com.natife.streaming.ui.player.PlayerViewModel
import com.natife.streaming.ui.player.PlayerViewModelImpl
import com.natife.streaming.ui.search.SearchViewModel
import com.natife.streaming.ui.search.SearchViewModelImpl
import com.natife.streaming.ui.search.gender.GenderViewModel
import com.natife.streaming.ui.search.gender.GenderViewModelImpl
import com.natife.streaming.ui.search.type.TypeDialogViewModel
import com.natife.streaming.ui.search.type.TypeDialogViewModelImpl
import com.natife.streaming.ui.settings.SettingsViewModel
import com.natife.streaming.ui.settings.SettingsViewModelImpl
import com.natife.streaming.ui.tournament.TournamentViewModel
import com.natife.streaming.usecase.*
import com.natife.streaming.utils.OneTimeScope
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import org.koin.android.ext.koin.androidApplication
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
    viewModel { (args: TournamentFragmentArgs) ->
        TournamentViewModel(
            args.sportId,
            args.tournamentId,
            get(),
            get(),
            get(),
            get())
    }
    viewModel<HomeViewModel> { HomeViewModelImpl(get(), get(), get()) }
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
    viewModel<CalendarViewModel> { CalendarViewModelImpl(get()) }
    viewModel<MatchProfileViewModel> { (args: MatchProfileFragmentArgs) ->
        MatchProfileViewModelImpl(
            args.sportId,
            args.matchId,
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel<MatchSettingsViewModel> { (args: MatchSettingsFragmentArgs) ->
        MatchSettingsViewModelImpl(
            args.match, args.sportId, args.videos,
            get(), get(), get()
        )
    }
    viewModel<WatchViewModel> { (args: WatchFragmentArgs) ->
        WatchViewModelImpl(
            args.match,
            args.video,
            get(),
            get(),
            get()
        )
    }
    viewModel<SearchViewModel> { SearchViewModelImpl(get(), get(), get(), get(), get(), get()) }
    viewModel<com.natife.streaming.ui.search.sport.SportViewModel> {
        com.natife.streaming.ui.search.sport.SportViewModelImpl(
            get(),
            get(),
            get()
        )
    }
    viewModel<TypeDialogViewModel> { TypeDialogViewModelImpl(get(), get(), get()) }
    viewModel<GenderViewModel> { GenderViewModelImpl(get(), get(), get()) }
    viewModel<PlayerViewModel> { (args: PlayerFragmentArgs) -> PlayerViewModelImpl(args.setup) }
    viewModel<SettingsViewModel> { SettingsViewModelImpl(get(), get(), get(), get(), get(),get()) }
    viewModel <FavoriteViewModel>{ FavoriteViewModelImpl(get(),get(),get()) }
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
    single(named(PREFS_MATCH_SETTINGS_QUALIFIER)) {
        androidContext().getSharedPreferences(
            PREFS_MATCH_SETTINGS_NAME,
            Context.MODE_PRIVATE
        )
    }
    single(named(PREFS_SEARCH_QUALIFIER)) {
        androidContext().getSharedPreferences(
            PREFS_SEARCH_NAME,
            Context.MODE_PRIVATE
        )
    }
    single { AuthPrefsImpl(get(named(PREFS_AUTH_QUALIFIER))) as AuthPrefs }
    single { SettingsPrefsImpl(get(named(PREFS_SETTINGS_QUALIFIER))) as SettingsPrefs }
    single { MatchSettingsPrefsImpl(get(named(PREFS_MATCH_SETTINGS_QUALIFIER))) as MatchSettingsPrefs }
    single { SearchPrefsImpl(get(named(PREFS_SEARCH_QUALIFIER))) as SearchPrefs }
}

val useCaseModule = module {
    factory<LoginUseCase> { LoginUseCaseImpl(get(), get()) }
    factory<LogoutUseCase> { LogoutUseCaseImpl(get(), get(), get()) }
    factory<AccountUseCase> { AccountUseCaseImpl(get(), get()) }
    factory<MatchUseCase> { MatchUseCaseImpl(get(), get()) }
    factory<GetShowScoreUseCase> { GetShowScoreUseCaseImpl() }
    factory<SaveShowScoreUseCase> { SaveShowScoreUseCaseImpl(get()) }
    factory<GetSportUseCase> { GetSportUseCaseImpl(get(), get()) }
    factory<SaveSportUseCase> { SaveSportUseCaseImpl(get()) }
    factory<GetLiveUseCase> { GetLiveUseCaseImpl() }
    factory<SaveLiveUseCase> { SaveLiveUseCaseImpl(get()) }
    factory<MatchProfileUseCase> { MatchProfileUseCaseImpl(get()) }
    factory<GetThumbnailUseCase> { GetThumbnailUseCaseImpl(get()) }
    factory<ActionsUseCase> { ActionsUseCaseImpl(get(), get()) }
    factory<GetTournamentUseCase> { GetTournamentUseCaseImpl(get()) }
    factory<SaveTournamentUseCase> { SaveTournamentUseCaseImpl(get()) }
    factory<TournamentUseCase> { TournamentUseCaseImpl(get()) }
    factory<SearchUseCase> { SearchUseCaseImpl(get(), get()) }
    factory<GenderUseCase> { GenderUseCaseImpl() }
    factory<SearchTypeUseCase> { SearchTypeUseCaseImpl() }
    factory<MatchInfoUseCase> { MatchInfoUseCaseImpl(get()) }
    factory<VideoUseCase> { VideoUseCaseImpl(get()) }
    factory<SecondUseCase> { SecondUseCaseImpl(get()) }
    factory<PlayerActionUseCase> { PlayerActionUseCaseImpl(get(), get(), get()) }
    factory<LexisUseCase> { LexisUseCaseImpl(get(), get(), get(), androidApplication()) }
    factory<CardUseCase> { CardUseCaseImpl() }
    factory<SubscriptionUseCase> { SubscriptionUseCaseImpl() }
    factory <SaveDeleteFavoriteUseCase>{ SaveDeleteFavoriteUseCaseImpl(get())}
    factory <FavoritesUseCase>{ FavoritesUseCaseImpl(get())}

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

    factory { AuthInterceptor(get()) }
    factory { ErrorInterceptor(get()) }

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

val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().actionDao() }
    single { get<AppDatabase>().searchDao() }
    single { get<AppDatabase>().lexicDao() }
}
val utilModule = module {
    factory { OneTimeScope() }
}

val appModules = arrayListOf(
    viewModelModule,
    prefsModule,
    useCaseModule,
    mockModule,
    dataSourceModule,
    routerModule,
    apiModule,
    databaseModule,
    utilModule
)