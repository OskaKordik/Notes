package com.natife.streaming.ui.mypreferences

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.natife.streaming.R
import com.natife.streaming.data.dto.tournament.TournamentTranslateDTO
import com.natife.streaming.databinding.ItemListOfTournamentsNewBinding
import com.natife.streaming.ext.bindFlagImage
import com.natife.streaming.ext.bindSportImage
import com.natife.streaming.utils.setMarginHorizontal
import com.natife.streaming.utils.setSize

class TournamentAdapter(private val onListOfTournamentsClickListener: ((tournament: TournamentTranslateDTO) -> Unit)) :
    ListAdapter<TournamentTranslateDTO, TournamentAdapter.TournamentAdapterViewHolder>(
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
            binding.flagOfCommandCountryImageL.bindFlagImage(data.country.id)
            binding.typeOfGameImageL.bindSportImage(data.sport)
            binding.nameOfComandTextL.text = data.name
            binding.countryNameTextL.text = data.country.name
            binding.checkImage.visibility = if (data.isCheck) View.VISIBLE else View.GONE
            itemView.setOnClickListener {
                onListOfTournamentsClickListener.invoke(data)
            }
            itemView.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.nameOfComandTextL.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                    binding.countryNameTextL.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f)
                    val margin = itemView.resources.getDimension(R.dimen.dp_6).toInt()
                    binding.itemTournament.setMarginHorizontal(margin)
                    val size = itemView.resources.getDimension(R.dimen.dp_12).toInt()
                    binding.typeOfGameImageL.setSize(size, size)
                } else {
                    binding.nameOfComandTextL.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                    binding.countryNameTextL.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9f)
                    val margin = itemView.resources.getDimension(R.dimen.dp_10).toInt()
                    binding.itemTournament.setMarginHorizontal(margin)
                    val size = itemView.resources.getDimension(R.dimen.dp_8).toInt()
                    binding.typeOfGameImageL.setSize(size, size)
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
        return oldItem.id == newItem.id && oldItem.sport == newItem.sport && oldItem.tournamentType == newItem.tournamentType
    }

    override fun areContentsTheSame(
        oldItem: TournamentTranslateDTO,
        newItem: TournamentTranslateDTO
    ): Boolean {
        return oldItem == newItem
    }

}