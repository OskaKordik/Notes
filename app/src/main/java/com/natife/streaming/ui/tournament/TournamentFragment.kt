package com.natife.streaming.ui.tournament

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.leanback.widget.BaseGridView
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.bindFlagImage
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ext.url
import com.natife.streaming.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_tournament.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class TournamentFragment: BaseFragment<TournamentViewModel>() {

    private val adapter : TournamentAdapter by lazy { TournamentAdapter(){
        viewModel.toProfile(it)
    } }
    override fun getLayoutRes() = R.layout.fragment_tournament

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).main_group?.visibility = View.GONE
        (activity as MainActivity).tournament_group?.visibility = View.VISIBLE

//        scoreBtn.setOnClickListener {
//            viewModel.onScoreClicked()
//        }

        subscribe(viewModel.tournament) {
            (activity as MainActivity).tournament_title_text?.text = it.title
            (activity as MainActivity).tournament_logo_image?.url(it.icon, it.placeholder)
            (activity as MainActivity).favorites_button?.icon = if (it.isFavorite) {
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_star
                )
            } else {
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_star_outline
                )
            }
            (activity as MainActivity).flag_of_command_country_image_l?.bindFlagImage(it.countryId)
            (activity as MainActivity).country_name_text_l?.text = it.countryName
            (activity as MainActivity).favorites_button?.setOnClickListener { v ->
                viewModel.addToFavorite(it)
            }
        }

        subscribe(viewModel.team) {
            (activity as MainActivity).tournament_title_text?.text = it.name
            (activity as MainActivity).tournament_logo_image?.url(it.image, it.placeholder)
            (activity as MainActivity).favorites_button?.icon = if (it.isFavorite) {
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_star
                )
            } else {
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_star_outline
                )
            }
            (activity as MainActivity).flag_of_command_country_image_l?.bindFlagImage(it.countryId)
            (activity as MainActivity).country_name_text_l?.text = it.countryName
            (activity as MainActivity).favorites_button?.setOnClickListener { v ->
                viewModel.addToFavorite(it)
            }
        }
        subscribe(viewModel.player) {
            (activity as MainActivity).tournament_title_text?.text = it.name
            (activity as MainActivity).tournament_logo_image?.url(it.image, it.imagePlaceholder)
            (activity as MainActivity).favorites_button?.icon = if (it.isFavorite) {
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_star
                )
            } else {
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_star_outline
                )
            }
            (activity as MainActivity).flag_of_command_country_image_l?.bindFlagImage(it.countryId)
            (activity as MainActivity).country_name_text_l?.text = it.countryName
            (activity as MainActivity).favorites_button?.setOnClickListener { v ->
                viewModel.addToFavorite(it)
            }
        }

        tournamentRecycler.focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ITEM
        tournamentRecycler.setNumColumns(4)
        tournamentRecycler.adapter = adapter
        adapter.onBind = {
            viewModel.loadList()
        }


        subscribe(viewModel.list, adapter::submitList)
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(TournamentFragmentArgs.fromBundle(requireArguments()))
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).main_group?.visibility = View.VISIBLE
        (activity as MainActivity).tournament_group?.visibility = View.GONE
    }
}