package com.natife.streaming.ui.matchprofile

import android.view.View
import com.natife.streaming.base.BaseViewHolder
import com.natife.streaming.data.actions.Action
import kotlinx.android.synthetic.main.item_action.view.*
import timber.log.Timber

class ActionViewHolder(view: View): BaseViewHolder<Action>(view) {
    override fun onBind(data: Action) {
        Timber.e("data $data")
        itemView.chackbox.text = data.name
        itemView.chackbox.isChecked = data.selected
    }
}