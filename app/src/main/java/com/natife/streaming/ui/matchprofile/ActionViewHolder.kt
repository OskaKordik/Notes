package com.natife.streaming.ui.matchprofile

import android.view.View
import com.natife.streaming.base.BaseViewHolder
import com.natife.streaming.data.actions.Action
import kotlinx.android.synthetic.main.item_action.view.*
import timber.log.Timber

class ActionViewHolder(view: View, private val onCheck: (Action) -> Unit, private val onlyClickable: Boolean): BaseViewHolder<Action>(view) {
    override fun onBind(data: Action) {
        Timber.e("data $data")
        itemView.chackbox.text = data.name
        itemView.chackbox.setOnCheckedChangeListener(null)
        itemView.chackbox.isChecked = data.selected
        if (onlyClickable){
            itemView.chackbox.setOnCheckedChangeListener { _, isChecked ->
                itemView.chackbox.isChecked = false
            }
            itemView.chackbox.setOnClickListener {
                onCheck.invoke(data)
            }
        }else{
            itemView.chackbox.setOnCheckedChangeListener { _, isChecked ->
                onCheck.invoke(data.copy(selected = isChecked))
            }
        }

    }


    override fun onBind(data: Action, payloads: List<Any>) {
        super.onBind(data, payloads)
        onBind(data)
    }
}