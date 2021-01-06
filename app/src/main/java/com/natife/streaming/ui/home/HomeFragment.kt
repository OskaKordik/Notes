package com.natife.streaming.ui.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber

class HomeFragment : BaseFragment<HomeViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_home

    val adapter: MatchAdapter by lazy { MatchAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeRecycler.layoutManager = GridLayoutManager(context, 4)
        homeRecycler.adapter = adapter
        Timber.e("before subscrube")
        subscribe(viewModel.list) {
            adapter.submitData(this.lifecycle, it)
        }
        Timber.e("aftersubscrube")
    }
}