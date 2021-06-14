package com.natife.streaming.ui.popupmatch.video.watch

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.card.MaterialCardView
import com.natife.streaming.R
import com.natife.streaming.base.BaseListAdapter
import com.natife.streaming.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_timed_button.view.*
import kotlinx.android.synthetic.main.view_timed_button.view.timedButtonText
import kotlinx.android.synthetic.main.view_timed_button.view.timedButtonTime

class TabWatchAdapter :
    BaseListAdapter<WatchFill, TabWatchAdapter.WatchFillViewHolder>(TabWatchAdapterDiffUtil()) {
    var onClick: ((WatchFill) -> Unit)? = null
//    val start = Event(true)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchFillViewHolder {
        return WatchFillViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_timed_button, parent, false),
            viewType
        )
    }

    override fun onBindViewHolder(holder: WatchFillViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            onClick?.invoke(currentList[position])
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewAttachedToWindow(holder: WatchFillViewHolder) {
        super.onViewAttachedToWindow(holder)
//        if (start.getContentIfNotHandled()==true) holder.itemView.requestFocus()
        holder.itemView.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            (view as MaterialCardView).timed_card.background = if (hasFocus) {
                view.context.resources.getDrawable(R.drawable.background_button_fild_white, null)
            } else {
                view.context.resources.getDrawable(R.drawable.background_button_fild_grey, null)
            }
            (view as MaterialCardView).timedButtonText.setTextColor(
                if (hasFocus) {
                    view.context.resources.getColor(R.color.black, null)
                } else {
                    view.context.resources.getColor(R.color.white, null)
                }
            )
            (view as MaterialCardView).timedButtonTime.setTextColor(
                if (hasFocus) {
                    view.context.resources.getColor(R.color.gray, null)
                } else {
                    view.context.resources.getColor(R.color.white40, null)
                }
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is WatchFill.FullGame -> 1
            is WatchFill.BallInPlay -> 2
            is WatchFill.Highlights -> 3
            is WatchFill.FieldGoals -> 4

        }
    }

    inner class WatchFillViewHolder(view: View, val viewType: Int) :
        BaseViewHolder<WatchFill>(view) {
        override fun onBind(data: WatchFill) {
            when (viewType) {
                1 -> {
                    (data as WatchFill.FullGame).apply {
                        itemView.timedButtonText.text = this.name
                        itemView.timedButtonTime.text = this.time
                    }
                }
                2 -> {
                    (data as WatchFill.BallInPlay).apply {
                        itemView.timedButtonText.text = this.name
                        itemView.timedButtonTime.text = this.time
                    }
                }
                3 -> {
                    (data as WatchFill.Highlights).apply {
                        itemView.timedButtonText.text = this.name
                        itemView.timedButtonTime.text = this.time
                    }
                }
                4 -> {
                    (data as WatchFill.FieldGoals).apply {
                        itemView.timedButtonText.text = this.name
                        itemView.timedButtonTime.text = this.time
                    }
                }
            }
        }

    }
}

class TabWatchAdapterDiffUtil : DiffUtil.ItemCallback<WatchFill>() {
    override fun areItemsTheSame(oldItem: WatchFill, newItem: WatchFill): Boolean {
        return when {
            oldItem is WatchFill.FullGame && newItem is WatchFill.FullGame -> oldItem.name == newItem.name && oldItem.time == newItem.time
            oldItem is WatchFill.BallInPlay && newItem is WatchFill.BallInPlay -> oldItem.name == newItem.name && oldItem.time == newItem.time
            oldItem is WatchFill.Highlights && newItem is WatchFill.Highlights -> oldItem.name == newItem.name && oldItem.time == newItem.time
            oldItem is WatchFill.FieldGoals && newItem is WatchFill.FieldGoals -> oldItem.name == newItem.name && oldItem.time == newItem.time
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: WatchFill, newItem: WatchFill): Boolean {
        return when {
            oldItem is WatchFill.FullGame && newItem is WatchFill.FullGame -> oldItem.name == newItem.name && oldItem.time == newItem.time
            oldItem is WatchFill.BallInPlay && newItem is WatchFill.BallInPlay -> oldItem.name == newItem.name && oldItem.time == newItem.time
            oldItem is WatchFill.Highlights && newItem is WatchFill.Highlights -> oldItem.name == newItem.name && oldItem.time == newItem.time
            oldItem is WatchFill.FieldGoals && newItem is WatchFill.FieldGoals -> oldItem.name == newItem.name && oldItem.time == newItem.time
            else -> false
        }
    }

}
