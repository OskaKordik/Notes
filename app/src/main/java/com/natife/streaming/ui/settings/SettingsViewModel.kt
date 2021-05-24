package com.natife.streaming.ui.settings


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.Card
import com.natife.streaming.data.Lexic
import com.natife.streaming.data.Sport
import com.natife.streaming.data.Subscription
import com.natife.streaming.data.actions.Action
import com.natife.streaming.preferenses.SettingsPrefs
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.CardUseCase
import com.natife.streaming.usecase.GetSportUseCase
import com.natife.streaming.usecase.LexisUseCase
import com.natife.streaming.usecase.SubscriptionUseCase
import timber.log.Timber
import java.util.*

abstract class SettingsViewModel : BaseViewModel() {

    abstract fun getSports()
    abstract fun getCards()
    abstract fun getStrings()
    abstract fun getSubscription()
    abstract fun getAddSubscription(prefix: String)
    abstract fun getLanguage(prefix: String)
    abstract fun getCountry(prefix: String)
    abstract fun selectSport(sport: Action)
    abstract fun selectCard(card: Action)
    abstract fun selectSubscription(sub: Action)
    abstract fun goToAdditionalSubs()
    abstract fun goToLanguageDialog()
    abstract fun goToCountryDialog()
    abstract fun saveCountry(country: Locale)
    abstract fun saveLanguage(locale: Locale)

    abstract val strings: LiveData<List<Lexic>>
    abstract val sports: LiveData<List<Action>>
    abstract val cards: LiveData<List<Action>>
    abstract val subscriptions: LiveData<List<Action>>
    abstract val additionalSub: LiveData<List<Action>>
    abstract val language : LiveData<List<Action>>
    abstract val country : LiveData<List<Action>>
    //abstract fun getStrings()
}

class SettingsViewModelImpl(
    private val sportUseCase: GetSportUseCase,
    private val lexisUseCase: LexisUseCase,
    private val cardUseCase: CardUseCase,
    private val subscriptionUseCase: SubscriptionUseCase,
    private val prefs: SettingsPrefs,
    private val router: Router
) : SettingsViewModel() {

    override val strings = MutableLiveData<List<Lexic>>()
    private var _sports = listOf<Sport>()
    private var _cards =  listOf<Card>()
    private var _subscriptions =  listOf<Subscription>()
    private var sub = listOf<Subscription>()
    override val sports = MutableLiveData<List<Action>>()
    override val cards = MutableLiveData<List<Action>>()
    override val subscriptions = MutableLiveData<List<Action>>()
    override val additionalSub = MutableLiveData<List<Action>>()
    override val language = MutableLiveData<List<Action>>()
    override val country = MutableLiveData<List<Action>>()


    init {
        launchCatching {
            collect(prefs.getSportFlow()){current->
                sports.value  = _sports.map {
                    Action(
                        id = it.id,
                        name = it.name,
                        selected = current == it.id
                    )
                }
            }
        }
        launchCatching {
            collect(prefs.getCardFlow()){current->
                cards.value  = _cards.map {
                    Action(
                        id = it.id,
                        name = it.name,
                        selected = prefs.getCard() == it.id
                    )
                }
            }
        }
        launchCatching {
            collect(prefs.getSubscriptionFlow()){current->
                subscriptions.value  = _subscriptions.map {
                    Action(
                        id = it.id,
                        name = it.name,
                        selected = current == it.id
                    )
                }
            }
        }
        launchCatching {
            collect(prefs.getCountryFlow()){current->
                Timber.e("KDMLKDMDK $current")
                country.value  = listOf(Action(id=-1,

                    Locale("",prefs.getCountry()).getDisplayCountry(Locale(prefs.getLanguage())),false))
                }
            }

        launchCatching {
            collect(prefs.getLanguageFlow()){current->
                language.value = listOf(Action(id=-1," ${Locale(prefs.getLanguage()).getDisplayLanguage(Locale(prefs.getLanguage()))}",false))
            }
        }

    }

    override fun getStrings() {
        launch {
            val str = lexisUseCase.execute(
                R.integer.sport,
                R.integer.payment,
                R.integer.subscriptions,
                R.integer.choose_subscription,
                R.integer.country,
                R.integer.selection_language
            )
            strings.value = str

        }
    }

    override fun getSubscription() {
        launch {

            _subscriptions  = subscriptionUseCase.execute()
            subscriptions.value  = _subscriptions.map {
                Action(
                    id = it.id,
                    name = it.name,
                    selected = prefs.getSubscription() == it.id
                )
            }
        }
    }

    override fun getAddSubscription(prefix: String) {
        launchCatching {
             sub = subscriptionUseCase.executeAdditional()
            additionalSub.value = listOf(Action(id=-1,"$prefix: ${sub.size}",false))
        }
    }

    override fun getLanguage(prefix: String) {
        launchCatching {
            language.value = listOf(Action(id=-1,"${Locale(prefs.getLanguage()).getDisplayLanguage(Locale(prefs.getLanguage()))}",false))
        }
    }

    override fun getCountry(prefix: String) {
        country.value = listOf(Action(id=-1,"${Locale("",prefs.getCountry()).getDisplayCountry(Locale(prefs.getLanguage()))}",false))
    }

    override fun selectSport(sport: Action) {
        prefs.saveSport(sport.id)
    }

    override fun selectCard(card: Action) {
        prefs.saveCard(card.id)
    }

    override fun selectSubscription(sub: Action) {
        prefs.saveSubscription(sub.id)
    }

    override fun goToAdditionalSubs() {
        router.navigate(SettingsFragmentDirections.actionSettingsFragmentToAditionalDialog(sub.toTypedArray()))
    }

    override fun goToLanguageDialog() {
        router.navigate(R.id.action_settingsFragment_to_languageDialog)
    }

    override fun goToCountryDialog() {
        router.navigate(R.id.action_settingsFragment_to_countryDialog)
    }

    override fun saveCountry(country: Locale) {
        Timber.e("poskdpokdpodksd ${country.country}")
        prefs.saveCountry(country.country)
        router.navigateUp()
    }

    override fun saveLanguage(locale: Locale) {
        prefs.saveLanguage(locale.language)
        router.navigateUp()
    }

    override fun getSports() {
        launch {

             _sports  = sportUseCase.execute()
            sports.value  = _sports.map {
                Action(
                    id = it.id,
                    name = it.name,
                    selected = prefs.getSport() == it.id
                )
            }
        }

    }

    override fun getCards() {
        launch {

            _cards  = cardUseCase.execute()
            cards.value  = _cards.map {
                Action(
                    id = it.id,
                    name = it.name,
                    selected = prefs.getCard() == it.id
                )
            }
        }
    }


}