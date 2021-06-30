package com.natife.streaming.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.leanback.widget.BaseGridView
import androidx.leanback.widget.VerticalGridView
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ext.subscribeEvent
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<HomeViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_home

    @SuppressLint("RestrictedApi")
    override fun onStart(){
        super.onStart()
        val adapter = TournamentAdapter(viewModel)
        homeRecycler.windowAlignment = VerticalGridView.WINDOW_ALIGN_BOTH_EDGE;
        homeRecycler.apply {
            this.requestFocus()
            isSelected = true
        }
        homeRecycler.isFocusable = false
        homeRecycler.adapter = adapter
        homeRecycler.setNumColumns(1)
        homeRecycler.focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ITEM


            viewModel.loadList()

        subscribe(viewModel.listTournament) {
            adapter.submitList(it)
            if ((it.isEmpty()) && (!viewModel.load))
                textView_match_not_find.visibility = View.VISIBLE
            else textView_match_not_find.visibility = View.GONE
        }
        subscribeEvent(viewModel.isLoadData) {
            progress_icon.visibility = if (it) View.GONE else View.VISIBLE
        }
    }


}