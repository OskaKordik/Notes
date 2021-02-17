package com.natife.streaming.ui.settings.pages.additionaldialog

import android.view.View
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewHolder
import com.natife.streaming.data.Subscription
import kotlinx.android.synthetic.main.item_action.view.*

class SubscriptionViewHolder(view: View): BaseViewHolder<Subscription>(view) {
    override fun onBind(data: Subscription) {
        itemView.chackbox.setOnCheckedChangeListener(null)
        itemView.chackbox.text = "${data.name} ${data.price} р/месяц"
        itemView.chackbox.isChecked = data.isBayed
        itemView.chackbox.setBackgroundResource(R.drawable.background_checkbox_trans)
    }
}