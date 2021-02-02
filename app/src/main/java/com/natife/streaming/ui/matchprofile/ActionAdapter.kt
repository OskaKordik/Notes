package com.natife.streaming.ui.matchprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.natife.streaming.R
import com.natife.streaming.base.BaseListAdapter
import com.natife.streaming.data.actions.Action
import kotlinx.android.synthetic.main.item_action.view.*

class ActionAdapter(private val onCheck: ((Action) -> Unit)? = null) : BaseListAdapter<Action, ActionViewHolder>(ActionDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        return ActionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_action, parent, false)
        ).apply {
            itemView.chackbox.setOnCheckedChangeListener { _, isChecked ->
                onCheck?.invoke(currentList[bindingAdapterPosition].copy(selected = isChecked))
            }
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
        val payloads = mutableListOf<Any>()
        if (oldItem.selected != newItem.selected){
            payloads.add(SELECTED_CHANGED_PAYLOAD)

        }
        return payloads
    }
    object SELECTED_CHANGED_PAYLOAD
}
