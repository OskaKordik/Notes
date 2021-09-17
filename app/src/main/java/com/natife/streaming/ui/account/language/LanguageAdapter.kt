package com.natife.streaming.ui.account.language

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.natife.streaming.R
import com.natife.streaming.db.entity.LanguageModel

class LanguageAdapter(private val onClick: (LanguageModel) -> Unit) : ListAdapter<LanguageModel, LanguageAdapter.LanguageViewHolder>(LanguageDiffCallBack) {

    class LanguageViewHolder(itemView: View, val onClick: (LanguageModel) -> Unit) : RecyclerView.ViewHolder(itemView) {

        private val tv = itemView.findViewById<TextView>(R.id.tvLang)
        private val fl = itemView.findViewById<FrameLayout>(R.id.flItemLanguage)

        fun bind(language: LanguageModel) {

            tv.text = language.language

            if(language.isCurrent) {
                tv.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 38f)
                fl.background = ContextCompat.getDrawable(itemView.context, R.drawable.round_white_background)
            } else {
                tv.setTextColor(ContextCompat.getColor(itemView.context, R.color.text_gray_selection))
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 31f)
                fl.background = ContextCompat.getDrawable(itemView.context, R.color.black)
            }

            itemView.setOnClickListener {
                onClick(language)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_language, parent, false)
        return LanguageViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language = getItem(position)
        holder.bind(language)
    }
}

object LanguageDiffCallBack : DiffUtil.ItemCallback<LanguageModel>(){
    override fun areItemsTheSame(oldItem: LanguageModel, newItem: LanguageModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: LanguageModel, newItem: LanguageModel): Boolean {
        return oldItem.id == newItem.id
    }
}
