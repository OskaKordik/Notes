package com.natife.streaming.ui.billing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.router.Router

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
) : BillingViewModel() {
    override val monthList = MutableLiveData<List<OfferData>>()
    override val title = MutableLiveData<String>()
    override val yearList = MutableLiveData<List<OfferData>>()
    override val payPerViewList = MutableLiveData<List<OfferData>>()

    init {
        title.value = "$team1 - $team2"
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
                "USD/montch",
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
                "USD/montch",
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
                "USD/montch",
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