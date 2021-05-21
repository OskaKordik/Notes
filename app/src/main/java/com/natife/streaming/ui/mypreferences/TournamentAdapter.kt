package com.natife.streaming.ui.mypreferences

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.natife.streaming.data.dto.tournament.TournamentTranslateDTO
import com.natife.streaming.databinding.ItemListOfTournamentsNewBinding

class TournamentAdapter(private val onListOfTournamentsClickListener: ((tournament: TournamentTranslateDTO, isCheck: Boolean) -> Unit)) :
    ListAdapter<Pair<TournamentTranslateDTO, TournamentTranslateDTO>, TournamentAdapter.TournamentAdapterViewHolder>(
        TournamentAdapterDiffUtilCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentAdapterViewHolder {
        val binding =
            ItemListOfTournamentsNewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return TournamentAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TournamentAdapterViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class TournamentAdapterViewHolder(
        private val binding: ItemListOfTournamentsNewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Pair<TournamentTranslateDTO, TournamentTranslateDTO>) {
            binding.nameOfComandTextL.text = data.first.name
            binding.countryNameTextL.text = data.first.country.name

            binding.nameOfComandTextR.text = data.second.name
            binding.countryNameTextR.text = data.second.country.name
        }
    }
}

class TournamentAdapterDiffUtilCallback :
    DiffUtil.ItemCallback<Pair<TournamentTranslateDTO, TournamentTranslateDTO>>() {
    override fun areItemsTheSame(
        oldItem: Pair<TournamentTranslateDTO, TournamentTranslateDTO>,
        newItem: Pair<TournamentTranslateDTO, TournamentTranslateDTO>
    ): Boolean {
        return oldItem.first.id == newItem.first.id && oldItem.second.id == newItem.second.id
    }

    override fun areContentsTheSame(
        oldItem: Pair<TournamentTranslateDTO, TournamentTranslateDTO>,
        newItem: Pair<TournamentTranslateDTO, TournamentTranslateDTO>
    ): Boolean {
        return oldItem == newItem
    }

}