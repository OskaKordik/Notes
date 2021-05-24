package com.natife.streaming.ui.mypreferences

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.natife.streaming.data.dto.tournament.TournamentTranslateDTO
import com.natife.streaming.databinding.ItemListOfTournamentsNewBinding

class TournamentAdapter(private val onListOfTournamentsClickListener: ((tournament: TournamentTranslateDTO, isCheck: Boolean) -> Unit)) :
    ListAdapter <TournamentTranslateDTO, TournamentAdapter.TournamentAdapterViewHolder>(
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
        fun bind(data: TournamentTranslateDTO) {
            binding.nameOfComandTextL.text = data.name
            binding.countryNameTextL.text = data.country.name
            binding.checkImage.visibility = if (data.isCheck) View.VISIBLE else View.GONE
            itemView.setOnClickListener {
                when (binding.checkImage.isVisible) {
                    true -> {
                        binding.checkImage.isVisible = false
                        onListOfTournamentsClickListener.invoke(data,false)
                    }
                    false -> {
                        binding.checkImage.isVisible= true
                        onListOfTournamentsClickListener.invoke(data, true)
                    }
                }
            }
        }
    }
}

class TournamentAdapterDiffUtilCallback :
    DiffUtil.ItemCallback<TournamentTranslateDTO>() {
    override fun areItemsTheSame(
        oldItem: TournamentTranslateDTO,
        newItem: TournamentTranslateDTO
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: TournamentTranslateDTO,
        newItem: TournamentTranslateDTO
    ): Boolean {
        return oldItem == newItem
    }

}