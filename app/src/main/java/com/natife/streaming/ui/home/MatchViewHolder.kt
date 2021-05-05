package com.natife.streaming.ui.home

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.text.color
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

        @ColorRes
        val colorId = when(data.sportId){
            1 -> R.color.football
            2 -> R.color.hockey
            3 -> R.color.basketball
            else -> R.color.text_accent
        }

        span.color(
            itemView.resources.getColor(colorId, null)
        ) {
            append(data.sportName.toUpperCase()) }
        span.append("   ")
        span.color(itemView.resources.getColor(R.color.text_gray, null)) { append(data.info) }
        itemView.matchDescription.text = span
        itemView.matchDescription.ellipsize = TextUtils.TruncateAt.MARQUEE
        itemView.matchDescription.isSelected = true

        Timber.e("data.date ${data.date} ${Date()} ${data.date.fromResponse().time}  ${data.date.fromResponse().time - Date().time}  ${Date().time}")

        val timeBeforeStart = (data.date.fromResponse().time - Date().time)

        //itemView.matchAlert.isVisible = !data.hasVideo  &&  timeBeforeStart > 0 &&  timeBeforeStart <1000*60*60
        itemView.matchAlert.text = data.date.fromResponse().toDisplay2()
        if (!data.access) {
           itemView.messageContainer.addView(Alert(itemView.context))
        }
    }

    override fun onBind(data: Match, payloads: List<Any>) {
        super.onBind(data, payloads)
        payloads.forEach {
            (it as List<Any>).forEach {

                if (it is MatchDiffUtil.IMAGE_PAYLOAD){
                    itemView.matchImage.url(data.image,data.placeholder)
                }
                if(it is MatchDiffUtil.INFO_PAYLOAD || it is MatchDiffUtil.SPORT_PAYLOAD){
                    val span = SpannableStringBuilder()
                    span.addTextColor(data.sportName, itemView.context)
//                    span.color(
//                        itemView.resources.getColor(R.color.text_accent, null)
//                    ) {
//                        append(data.sportName.toUpperCase()) }
                    span.append("   ")
                    span.color(itemView.resources.getColor(R.color.text_gray, null)) { append(data.info)}
                    itemView.matchDescription.text = span
                }
            }
        }
    }

    override fun onRecycled() {
        super.onRecycled()
        itemView.messageContainer.removeAllViews()
    }
}

private fun SpannableStringBuilder.addTextColor(sportName: String, context: Context) {
    when(sportName.trim().toUpperCase()){
       "ФУТБОЛ" -> this.color(context.resources.getColor(R.color.accentFutbol, context.resources.newTheme())){append(sportName.toUpperCase()) }
       "БАСКЕТБОЛ" -> this.color(context.resources.getColor(R.color.accentBasket, context.resources.newTheme())){append(sportName.toUpperCase()) }
        "ХОККЕЙ" -> this.color(context.resources.getColor(R.color.accentHockey, context.resources.newTheme())){ append(sportName.toUpperCase()) }
        else ->this.color(context.resources.getColor(R.color.text_accent, context.resources.newTheme())){append(sportName.toUpperCase()) }
    }
}
