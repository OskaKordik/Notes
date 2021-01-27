package com.natife.streaming.ui.matchprofile

import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.match.Match

abstract class MatchProfileViewModel: BaseViewModel() {
}

class MatchProfileViewModelImpl(private val match: Match): MatchProfileViewModel(){

}