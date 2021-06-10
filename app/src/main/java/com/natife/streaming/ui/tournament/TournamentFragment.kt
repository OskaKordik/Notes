package com.natife.streaming.ui.tournament

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.leanback.widget.BaseGridView
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.bindFlagImage
import com.natife.streaming.ext.predominantColorToGradient
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ext.url
import com.natife.streaming.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_tournament.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf


class TournamentFragment : BaseFragment<TournamentViewModel>() {
    private val adapter: TournamentAdapter by lazy {
        TournamentAdapter() {
            viewModel.toProfile(it)
        }
    }

    override fun getLayoutRes() = R.layout.fragment_tournament

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val WBColorStateList = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_focused), intArrayOf()),
            intArrayOf(
                requireContext().resources.getColor(R.color.black, requireContext().theme),
                requireContext().resources.getColor(R.color.white, requireContext().theme)
            )
        )
        (activity as MainActivity).main_group?.visibility = View.GONE
        (activity as MainActivity).tournament_group?.visibility = View.VISIBLE
        //Heading in the predominant team color
        (activity as MainActivity).mainMotion?.predominantColorToGradient("#CCCB312A")

        subscribe(viewModel.tournament) {
//            viewModel.gepProfileColor(1,1,1)
            (activity as MainActivity).tournament_title_text?.text = it.title
            (activity as MainActivity).tournament_logo_image?.url(it.icon, it.placeholder)
            (activity as MainActivity).favorites_button?.apply {
                when (it.isFavorite) {
                    true -> {
                        icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_star)
                        strokeColor = ColorStateList.valueOf(
                            resources.getColor(
                                R.color.white,
                                requireContext().theme
                            )
                        )
                        iconTint = WBColorStateList
                        setTextColor(WBColorStateList)
                    }
                    false -> {
                        icon =
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_outline)
                        strokeColor = ColorStateList.valueOf(
                            resources.getColor(
                                R.color.black_transparent_30,
                                requireContext().theme
                            )
                        )
                        iconTint = ColorStateList.valueOf(
                            resources.getColor(
                                R.color.black_transparent_30,
                                requireContext().theme
                            )
                        )
                        setTextColor(
                            resources.getColor(
                                R.color.black_transparent_30,
                                requireContext().theme
                            )
                        )
                    }
                }
                (activity as MainActivity).flag_of_command_country_image_l?.bindFlagImage(it.countryId)
                (activity as MainActivity).country_name_text_l?.text = it.countryName
                (activity as MainActivity).favorites_button?.setOnClickListener { v ->
                    viewModel.addToFavorite(it)
                }
            }
        }

        subscribe(viewModel.team) {
            (activity as MainActivity).tournament_title_text?.text = it.name
            (activity as MainActivity).tournament_logo_image?.url(it.image, it.placeholder)
            (activity as MainActivity).favorites_button?.apply {
                when (it.isFavorite) {
                    true -> {
                        icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_star)
                        strokeColor = ColorStateList.valueOf(
                            resources.getColor(
                                R.color.white,
                                requireContext().theme
                            )
                        )
                        iconTint = WBColorStateList
                        setTextColor(WBColorStateList)
                    }
                    false -> {
                        icon =
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_outline)
                        strokeColor = ColorStateList.valueOf(
                            resources.getColor(
                                R.color.black_transparent_30,
                                requireContext().theme
                            )
                        )
                        iconTint = ColorStateList.valueOf(
                            resources.getColor(
                                R.color.black_transparent_30,
                                requireContext().theme
                            )
                        )
                        setTextColor(
                            resources.getColor(
                                R.color.black_transparent_30,
                                requireContext().theme
                            )
                        )
                    }
                }
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
            (activity as MainActivity).favorites_button?.apply {
                when (it.isFavorite) {
                    true -> {
                        icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_star)
                        strokeColor = ColorStateList.valueOf(
                            resources.getColor(
                                R.color.white,
                                requireContext().theme
                            )
                        )
                        iconTint = WBColorStateList
                        setTextColor(WBColorStateList)
                    }
                    false -> {
                        icon =
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_outline)
                        strokeColor = ColorStateList.valueOf(
                            resources.getColor(
                                R.color.black_transparent_30,
                                requireContext().theme
                            )
                        )
                        iconTint = ColorStateList.valueOf(
                            resources.getColor(
                                R.color.black_transparent_30,
                                requireContext().theme
                            )
                        )
                        setTextColor(
                            resources.getColor(
                                R.color.black_transparent_30,
                                requireContext().theme
                            )
                        )
                    }
                }
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
        (activity as MainActivity).mainMotion?.predominantColorToGradient("#3560E1")
    }
}