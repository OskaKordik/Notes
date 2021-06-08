package com.natife.streaming.ui.home

//@Deprecated("Don't uses")
//open class MatchViewHolder(view: View) : BaseViewHolder<Match>(view) {
//
//    override fun onBind(data: Match) {
//        itemView.matchImage.url(data.image,data.placeholder)
//        itemView.matchTitle.text = "${data.team1.name} - ${data.team2.name}"
//        val span = SpannableStringBuilder()
//
//        span.color(
//            itemView.resources.getColor(selectSportColor(data.sportId), null)
//        ) {
//            append(data.sportName.toUpperCase()) }
//
//        span.append("   ")
//
//        span.color(itemView.resources.getColor(R.color.text_gray, null)) { append(data.info) }
//        itemView.matchDescription.text = span
//        itemView.matchDescription.ellipsize = TextUtils.TruncateAt.MARQUEE
//        itemView.matchDescription.isSelected = true
//
//        Timber.e("data.date ${data.date} ${Date()} ${data.date.fromResponse().time}  ${data.date.fromResponse().time - Date().time}  ${Date().time}")
//
//        val timeBeforeStart = (data.date.fromResponse().time - Date().time)
//
//        //itemView.matchAlert.isVisible = !data.hasVideo  &&  timeBeforeStart > 0 &&  timeBeforeStart <1000*60*60
//        itemView.matchAlert.text = data.date.fromResponse().toDisplay2()
//        if (!data.access) {
//            itemView.messageContainer.addView(Alert(itemView.context))
//        }
//    }

//    override fun onBind(data: Match, payloads: List<Any>) {
//        super.onBind(data, payloads)
//        payloads.forEach {
//            (it as List<Any>).forEach {
//
//                if (it is MatchDiffUtil.IMAGE_PAYLOAD){
//                    itemView.matchImage.url(data.image,data.placeholder)
//                }
//                if(it is MatchDiffUtil.INFO_PAYLOAD || it is MatchDiffUtil.SPORT_PAYLOAD){
//                    val span = SpannableStringBuilder()
//                    span.color(
//                        itemView.resources.getColor(selectSportColor(data.sportId), null)
//                    ) {
//                        append(data.sportName.toUpperCase()) }
//                    span.append("   ")
//                    span.color(itemView.resources.getColor(R.color.text_gray, null)) { append(data.info)}
//                    itemView.matchDescription.text = span
//                }
//            }
//        }
//    }

//    override fun onRecycled() {
//        super.onRecycled()
//        itemView.messageContainer.removeAllViews()
//    }
//}
//
//private fun selectSportColor(sportId: Int):Int {
//    return when(sportId){
//        1 -> R.color.football
//        2 -> R.color.hockey
//        3 -> R.color.basketball
//        else -> R.color.text_accent
//    }
//}

