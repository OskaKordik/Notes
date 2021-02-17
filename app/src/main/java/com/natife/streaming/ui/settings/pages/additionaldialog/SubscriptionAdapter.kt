package com.natife.streaming.ui.settings.pages.additionaldialog


import android.view.LayoutInflater
import android.view.ViewGroup
import com.natife.streaming.R
import com.natife.streaming.base.BaseAdapter
import com.natife.streaming.base.BaseViewHolder
import com.natife.streaming.data.Subscription
import com.natife.streaming.ui.matchprofile.ActionViewHolder

class SubscriptionAdapter : BaseAdapter<Subscription>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<Subscription> {
        return SubscriptionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_action, parent, false))
    }
}