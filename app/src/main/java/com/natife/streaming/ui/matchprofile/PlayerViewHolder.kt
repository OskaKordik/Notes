package com.natife.streaming.ui.matchprofile

import android.view.View
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewHolder
import com.natife.streaming.data.matchprofile.Player
import com.natife.streaming.ext.url
import com.natife.streaming.ext.urlCircled
import kotlinx.android.synthetic.main.item_player.view.*

class PlayerViewHolder(view: View): BaseViewHolder<Player>(view) {
    override fun onBind(data: Player) {
        if(data.team == 1){
            itemView.playerImage.setBackgroundResource(R.drawable.player_first_background)
        }else{
            itemView.playerImage.setBackgroundResource(R.drawable.player_second_background)
        }
        itemView.playerImage.urlCircled(data.image,data.imagePlaceholder)
        itemView.playerName.text = "${data.number} ${data.name}"
    }
}