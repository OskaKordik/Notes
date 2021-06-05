package com.natife.streaming.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.leanback.widget.BaseGridView
import androidx.leanback.widget.VerticalGridView
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<HomeViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_home

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = TournamentAdapter(viewModel)
        homeRecycler.windowAlignment = VerticalGridView.WINDOW_ALIGN_BOTH_EDGE;

        homeRecycler.isFocusable = false
        homeRecycler.adapter = adapter
        homeRecycler.setNumColumns(1)
        homeRecycler.focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ITEM


        adapter.onBind = {
            val a = adapter.itemCount
            viewModel.loadList()
        }
        subscribe(viewModel.listTournament) {
            adapter.submitList(it)
        }

    }
}