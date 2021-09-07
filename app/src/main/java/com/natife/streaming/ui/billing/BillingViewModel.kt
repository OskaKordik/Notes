package com.natife.streaming.ui.billing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.dto.subscription.Month
import com.natife.streaming.data.dto.subscription.SubscribeResponse
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.GetMatchSubscriptionsUseCase

abstract class BillingViewModel : BaseViewModel() {
    abstract val matchId: Int
    abstract val sportId: Int
    abstract val tournamentId: Int
    abstract val tournamentTitle: String
    abstract val live: Boolean
    abstract val team1: String
    abstract val team2: String
    abstract val monthList: LiveData<List<OfferData>>
    abstract val yearList: LiveData<List<OfferData>>
    abstract val payPerViewList: LiveData<List<OfferData>>
    abstract val title: LiveData<String>
    abstract fun initData()
    abstract fun onFinishClicked()
    abstract fun select(result: OfferData)
}

class BillingViewModelImpl(
    override val matchId: Int,
    override val sportId: Int,
    override val tournamentId: Int,
    override val tournamentTitle: String,
    override val live: Boolean,
    override val team1: String,
    override val team2: String,
    private val router: Router,
    private val getMatchSubscriptionsUseCase: GetMatchSubscriptionsUseCase
) : BillingViewModel() {
    override val monthList = MutableLiveData<List<OfferData>>()
    override val title = MutableLiveData<String>()
    override val yearList = MutableLiveData<List<OfferData>>()
    override val payPerViewList = MutableLiveData<List<OfferData>>()
    private lateinit var subscribtionData: SubscribeResponse

    init {
        launch {
            subscribtionData = Gson().fromJson<SubscribeResponse>(
                "{\n" +
                        "  \"season\": {\n" +
                        "    \"id\": 45,\n" +
                        "    \"name\": \"2030\"\n" +
                        "  },\n" +
                        "  \"month\": {\n" +
                        "    \"all\": {\n" +
                        "      \"id\": 53,\n" +
                        "      \"lexic\": 1234,\n" +
                        "      \"currency_id\": 1,\n" +
                        "      \"currency_iso\": \"RUB\",\n" +
                        "      \"price\": 100,\n" +
                        "      \"sub\": {\n" +
                        "        \"id\": 53,\n" +
                        "        \"active_from\": \"2021-01-01\",\n" +
                        "        \"active_to\": \"2021-12-31\",\n" +
                        "        \"option\": 0,\n" +
                        "        \"currency_id\": 1,\n" +
                        "        \"price\": 100\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"team1\": {\n" +
                        "      \"id\": 53,\n" +
                        "      \"lexic\": 1234,\n" +
                        "      \"currency_id\": 1,\n" +
                        "      \"currency_iso\": \"RUB\",\n" +
                        "      \"price\": 200,\n" +
                        "      \"sub\": {\n" +
                        "        \"id\": 53,\n" +
                        "        \"active_from\": \"2021-01-01\",\n" +
                        "        \"active_to\": \"2021-12-31\",\n" +
                        "        \"option\": 1,\n" +
                        "        \"currency_id\": 1,\n" +
                        "        \"price\": 200,\n" +
                        "        \"sport\": 1,\n" +
                        "        \"team_id\": 21\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"team2\": {\n" +
                        "      \"id\": 53,\n" +
                        "      \"lexic\": 1234,\n" +
                        "      \"currency_id\": 1,\n" +
                        "      \"currency_iso\": \"RUB\",\n" +
                        "      \"price\": 200,\n" +
                        "      \"sub\": {\n" +
                        "        \"id\": 53,\n" +
                        "        \"active_from\": \"2021-01-01\",\n" +
                        "        \"active_to\": \"2021-12-31\",\n" +
                        "        \"option\": 1,\n" +
                        "        \"currency_id\": 1,\n" +
                        "        \"price\": 200,\n" +
                        "        \"sport\": 1,\n" +
                        "        \"team_id\": 248\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"team_home\": {\n" +
                        "      \"id\": 53,\n" +
                        "      \"lexic\": 1234,\n" +
                        "      \"currency_id\": 1,\n" +
                        "      \"currency_iso\": \"RUB\",\n" +
                        "      \"price\": 300,\n" +
                        "      \"sub\": {\n" +
                        "        \"id\": 53,\n" +
                        "        \"active_from\": \"2021-01-01\",\n" +
                        "        \"active_to\": \"2021-12-31\",\n" +
                        "        \"option\": 2,\n" +
                        "        \"currency_id\": 1,\n" +
                        "        \"price\": 300,\n" +
                        "        \"sport\": 1,\n" +
                        "        \"team_id\": 21\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"team_away\": {\n" +
                        "      \"id\": 53,\n" +
                        "      \"lexic\": 1234,\n" +
                        "      \"currency_id\": 1,\n" +
                        "      \"currency_iso\": \"RUB\",\n" +
                        "      \"price\": 300,\n" +
                        "      \"sub\": {\n" +
                        "        \"id\": 53,\n" +
                        "        \"active_from\": \"2021-01-01\",\n" +
                        "        \"active_to\": \"2021-12-31\",\n" +
                        "        \"option\": 3,\n" +
                        "        \"currency_id\": 1,\n" +
                        "        \"price\": 300,\n" +
                        "        \"sport\": 1,\n" +
                        "        \"team_id\": 248\n" +
                        "      }\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"year\": {\n" +
                        "    \"all\": {\n" +
                        "      \"id\": 53,\n" +
                        "      \"lexic\": 1234,\n" +
                        "      \"currency_id\": 1,\n" +
                        "      \"currency_iso\": \"RUB\",\n" +
                        "      \"price\": 1000,\n" +
                        "      \"sub\": {\n" +
                        "        \"id\": 53,\n" +
                        "        \"active_from\": \"2021-01-01\",\n" +
                        "        \"active_to\": \"2021-12-31\",\n" +
                        "        \"option\": 0,\n" +
                        "        \"currency_id\": 1,\n" +
                        "        \"price\": 1000\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"team1\": {\n" +
                        "      \"id\": 53,\n" +
                        "      \"lexic\": 1234,\n" +
                        "      \"currency_id\": 1,\n" +
                        "      \"currency_iso\": \"RUB\",\n" +
                        "      \"price\": 2000,\n" +
                        "      \"sub\": {\n" +
                        "        \"id\": 53,\n" +
                        "        \"active_from\": \"2021-01-01\",\n" +
                        "        \"active_to\": \"2021-12-31\",\n" +
                        "        \"option\": 1,\n" +
                        "        \"currency_id\": 1,\n" +
                        "        \"price\": 2000,\n" +
                        "        \"sport\": 1,\n" +
                        "        \"team_id\": 21\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"team2\": {\n" +
                        "      \"id\": 53,\n" +
                        "      \"lexic\": 1234,\n" +
                        "      \"currency_id\": 1,\n" +
                        "      \"currency_iso\": \"RUB\",\n" +
                        "      \"price\": 2000,\n" +
                        "      \"sub\": {\n" +
                        "        \"id\": 53,\n" +
                        "        \"active_from\": \"2021-01-01\",\n" +
                        "        \"active_to\": \"2021-12-31\",\n" +
                        "        \"option\": 1,\n" +
                        "        \"currency_id\": 1,\n" +
                        "        \"price\": 2000,\n" +
                        "        \"sport\": 1,\n" +
                        "        \"team_id\": 248\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"team_home\": {\n" +
                        "      \"id\": 53,\n" +
                        "      \"lexic\": 1234,\n" +
                        "      \"currency_id\": 1,\n" +
                        "      \"currency_iso\": \"RUB\",\n" +
                        "      \"price\": 3000,\n" +
                        "      \"sub\": {\n" +
                        "        \"id\": 53,\n" +
                        "        \"active_from\": \"2021-01-01\",\n" +
                        "        \"active_to\": \"2021-12-31\",\n" +
                        "        \"option\": 2,\n" +
                        "        \"currency_id\": 1,\n" +
                        "        \"price\": 3000,\n" +
                        "        \"sport\": 1,\n" +
                        "        \"team_id\": 21\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"team_away\": {\n" +
                        "      \"id\": 53,\n" +
                        "      \"lexic\": 1234,\n" +
                        "      \"currency_id\": 1,\n" +
                        "      \"currency_iso\": \"RUB\",\n" +
                        "      \"price\": 3000,\n" +
                        "      \"sub\": {\n" +
                        "        \"id\": 53,\n" +
                        "        \"active_from\": \"2021-01-01\",\n" +
                        "        \"active_to\": \"2021-12-31\",\n" +
                        "        \"option\": 3,\n" +
                        "        \"currency_id\": 1,\n" +
                        "        \"price\": 3000,\n" +
                        "        \"sport\": 1,\n" +
                        "        \"team_id\": 248\n" +
                        "      }\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"pay_per_view\": {\n" +
                        "    \"currency_id\": 1,\n" +
                        "    \"currency_iso\": \"RUB\",\n" +
                        "    \"price\": 500,\n" +
                        "    \"sub\": {\n" +
                        "      \"id\": 53,\n" +
                        "      \"active_from\": \"2021-01-01\",\n" +
                        "      \"active_to\": \"2021-12-31\",\n" +
                        "      \"option\": 4,\n" +
                        "      \"currency_id\": 1,\n" +
                        "      \"price\": 500,\n" +
                        "      \"sport\": 1,\n" +
                        "      \"match_id\": 1965982\n" +
                        "    }\n" +
                        "  }\n" +
                        "}", SubscribeResponse::class.java
            )
//            subscribtionData =
//                getMatchSubscriptionsUseCase.execute(sportId, matchId)// TODO пока не работает


            title.value = "$team1 - $team2"
            val a: List<OfferData>? = subscribtionData.month?.monthToOfferData()
        }
    }

    override fun initData() {
        monthList.value = listOf(
            OfferData(
                matchId,
                sportId,
                tournamentId,
                "League Pass",
                tournamentTitle,
                "All matches in season 2020-21, live and on demand",
                "USD/Month",
                150,
                live,
                "$team1 - $team2"
            ),
            OfferData(
                matchId,
                sportId,
                tournamentId,
                "Team pass",
                team1,
                "All matches of $team1 in season 2020-21, live and on demand",
                "USD/Month",
                70,
                live,
                "$team1 - $team2"
            ),
            OfferData(
                matchId,
                sportId,
                tournamentId,
                "Team Pass",
                team2,
                "All matches of $team2 in season 2020-21, live and on demand",
                "USD/Month",
                80,
                live,
                "$team1 - $team2"
            ),
        )
        yearList.value = listOf(
            OfferData(
                matchId,
                sportId,
                tournamentId,
                "League Pass",
                tournamentTitle,
                "All matches in season 2020-21, live and on demand",
                "USD/year",
                1500,
                live,
                "$team1 - $team2"
            ),
            OfferData(
                matchId,
                sportId,
                tournamentId,
                "Team pass",
                team1,
                "All matches of $team1 in season 2020-21, live and on demand",
                "USD/year",
                700,
                live,
                "$team1 - $team2"
            ),
            OfferData(
                matchId,
                sportId,
                tournamentId,
                "Team Pass",
                team2,
                "All matches of $team2 in season 2020-21, live and on demand",
                "USD/year",
                800,
                live,
                "$team1 - $team2"
            ),
        )
        payPerViewList.value = listOf(
            OfferData(
                matchId,
                sportId,
                tournamentId,
                "League Pass",
                tournamentTitle,
                "All matches in season 2020-21, live and on demand",
                "USD/view",
                15,
                live,
                "$team1 - $team2"
            ),
            OfferData(
                matchId,
                sportId,
                tournamentId,
                "Team pass",
                team1,
                "All matches of $team1 in season 2020-21, live and on demand",
                "USD/view",
                7,
                live,
                "$team1 - $team2"
            ),
            OfferData(
                matchId,
                sportId,
                tournamentId,
                "Team Pass",
                team2,
                "All matches of $team2 in season 2020-21, live and on demand",
                "USD/view",
                8,
                live,
                "$team1 - $team2"
            ),
        )
    }

    override fun onFinishClicked() {
        router.navigateUp()
    }

    override fun select(result: OfferData) {
        when (result.isLive) {
            true -> {
                router.navigate(
                    BillingFragmentDirections.actionBillingFragmentToLiveFragment(
                        matchId,
                        sportId,
                        result.titleForLivePlayer
                    )
                )
            }
            false -> {
                router.navigate(
                    BillingFragmentDirections.actionBillingFragmentToPopupVideo(matchId, sportId)
                )
            }

        }
    }
}

private fun Month.monthToOfferData(): List<OfferData> {
    return listOf()//TODO
}

data class OfferData(
    val matchId: Int = 0,
    val sportId: Int = 0,
    val tournamentId: Int = 0,
    val offerTitle: String = "",
    val teamLeagueName: String = "",
    val description: String = "",
    val billingOfferCurrency: String = "",
    val billingOfferPrice: Int = 0,
    val isLive: Boolean = false,
    val titleForLivePlayer: String = ""
)