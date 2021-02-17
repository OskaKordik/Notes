package com.natife.streaming.ui.matchprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.natife.streaming.R
import com.natife.streaming.base.BaseListAdapter
import com.natife.streaming.data.actions.Action
import kotlinx.android.synthetic.main.item_action.view.*

class ActionAdapter(private val onCheck: ((Action) -> Unit),private val isUncheckable: Boolean = true,private val onlyClickable: Boolean = false ) : BaseListAdapter<Action, ActionViewHolder>(ActionDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        return ActionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_action, parent, false),
            onCheck,
            onlyClickable
        )
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (!isUncheckable && !onlyClickable)
        {
            holder.itemView.chackbox.isClickable = !holder.itemView.chackbox.isChecked
        }
    }

    override fun onBindViewHolder(
        holder: ActionViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (!isUncheckable&& !onlyClickable)
        {
            holder.itemView.chackbox.isClickable = !holder.itemView.chackbox.isChecked
        }
    }

}

class ActionDiffUtil() : DiffUtil.ItemCallback<Action>() {
    override fun areItemsTheSame(oldItem: Action, newItem: Action): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Action, newItem: Action): Boolean {

        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Action, newItem: Action): Any? {
        Timber.e("kmdfmdfmdfmfkdm $oldItem $newItem")
        val payloads = mutableListOf<Any>()
        if (oldItem.selected != newItem.selected){
            payloads.add(SELECTED_CHANGED_PAYLOAD)

        }
        return payloads
    }
    object SELECTED_CHANGED_PAYLOAD
}
