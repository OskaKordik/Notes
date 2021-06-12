package com.natife.streaming.ui.popupmatch.video.additionaly

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.leanback.widget.BaseGridView
import androidx.leanback.widget.VerticalGridView
import androidx.navigation.navGraphViewModels
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.base.EmptyViewModel
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ui.popupmatch.PopupSharedViewModel
import kotlinx.android.synthetic.main.fragment_tab_additionally.*
import kotlinx.android.synthetic.main.item_episode.view.*


class TabAdditionallyFragment : BaseFragment<EmptyViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_tab_additionally
    private val popupSharedViewModel: PopupSharedViewModel by navGraphViewModels(R.id.popupVideo)
    private val episodeAdapter: EpisodeAdapter by lazy { EpisodeAdapter() }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        additionalMatchRecycler.windowAlignment = VerticalGridView.WINDOW_ALIGN_BOTH_EDGE
        additionalMatchRecycler.isFocusable = false
        additionalMatchRecycler.setNumColumns(4)
        additionalMatchRecycler.focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ITEM
        additionalMatchRecycler.adapter = episodeAdapter
        episodeAdapter.onClick = {
            popupSharedViewModel.playEpisode(it)
        }

        subscribe(popupSharedViewModel.episodes, episodeAdapter::submitList)
    }

    override fun onResume() {
        super.onResume()
        if (additionalMatchRecycler.findViewHolderForAdapterPosition(0)?.itemView != null) {
            (additionalMatchRecycler.findViewHolderForAdapterPosition(0)?.itemView as FrameLayout).additional_video.requestFocus()
        }
    }

    override fun onPause() {
        super.onPause()

    }
}