package com.natife.streaming.ui.home

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.text.color
import androidx.core.view.isVisible
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewHolder
import com.natife.streaming.custom.Alert
import com.natife.streaming.data.match.Match
import com.natife.streaming.ext.fromResponse
import com.natife.streaming.ext.toDisplay2
import com.natife.streaming.ext.url
import kotlinx.android.synthetic.main.item_match.view.*
import timber.log.Timber
import java.util.*

open class MatchViewHolder(view: View) : BaseViewHolder<Match>(view) {

    override fun onBind(data: Match) {
        itemView.matchImage.url(data.image,data.placeholder)
        itemView.matchTitle.text = "${data.team1.name} - ${data.team2.name}"
        val span = SpannableStringBuilder()
        span.color(
            itemView.resources.getColor(R.color.text_accent, null)
        ) {
            append(data.sportName.toUpperCase()) }
        span.append(" ")
        span.color(itemView.resources.getColor(R.color.text_gray, null)) { append(data.info) }
        itemView.matchDescription.text = span

        Timber.e("data.date ${data.date} ${Date()} ${data.date.fromResponse().time}  ${data.date.fromResponse().time - Date().time}  ${Date().time}")

        val timeBeforeStart = (data.date.fromResponse().time - Date().time)

        //itemView.matchAlert.isVisible = !data.hasVideo  &&  timeBeforeStart > 0 &&  timeBeforeStart <1000*60*60
        itemView.matchAlert.text = data.date.fromResponse().toDisplay2()
        if (!data.access) {
           itemView.messageContainer.addView(Alert(itemView.context))
        }
    }

    override fun onRecycled() {
        super.onRecycled()
        itemView.messageContainer.removeAllViews()
    }
}