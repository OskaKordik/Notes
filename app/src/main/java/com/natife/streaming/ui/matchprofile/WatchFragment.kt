package com.natife.streaming.ui.matchprofile

import android.os.Bundle
import android.view.View
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.base.EmptyViewModel
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.fragment_match_prewatch.*

import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class WatchFragment: BaseFragment<WatchViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_match_prewatch
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribe(viewModel.title,title::setText)
        buttonBack.setOnClickListener {
            viewModel.back()
        }
    }
    override fun getParameters(): ParametersDefinition = {
        parametersOf(WatchFragmentArgs.fromBundle(requireArguments()))
    }
}