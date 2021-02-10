package com.natife.streaming.ui.matchprofile

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.fragment_match_profile_settings.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class MatchSettingsFragment: BaseFragment<MatchSettingsViewModel>() {

    private lateinit var  adapter : ActionAdapter


    override fun getLayoutRes(): Int = R.layout.fragment_match_profile_settings

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ActionAdapter {
            viewModel.select(it)
        }
        actionRecycler.layoutManager = GridLayoutManager(context,3)
        actionRecycler.adapter = adapter

        buttonBack.setOnClickListener {
            viewModel.goBack()
        }

        subscribe(viewModel.actions,adapter::submitList)
        buttonAllTime.setOnClickListener {
            viewModel.goToPrewatch()
        }
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                buttonAllActions.id -> setupAll()
                buttonSelectedTime.id -> setupSelected()

            }
        }
    }

    override fun onStop() {
        Timber.e("stop")
        viewModel.save()
        groupActions.isVisible = false
        groupInterval.isVisible = false
        super.onStop()
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(MatchSettingsFragmentArgs.fromBundle(requireArguments()))
    }

    private fun setupAll(){
        interval.setAfter(viewModel.getAllAfter())
        interval.setBefore(viewModel.getAllBefore())
        interval.onAfterValueChanged = afterAll
        interval.onBeforeValueChanged= beforeAll
        groupActions.isVisible = false
        groupInterval.isVisible = true

    }
    private fun setupSelected(){
        interval.setAfter(viewModel.getSelectedAfter())
        interval.setBefore(viewModel.getSelectedBefore())
        interval.onAfterValueChanged = afterSelected
        interval.onBeforeValueChanged = beforeSelected
        groupActions.isVisible = true
        groupInterval.isVisible = true
    }


    private val afterAll: ((Int)->Unit)={
        viewModel.saveAllAfter(it)
    }
    private val beforeAll: ((Int)->Unit)={
        viewModel.saveAllBefore(it)
    }
    private val afterSelected: ((Int)->Unit)={
        viewModel.saveSelectedAfter(it)
    }
    private val beforeSelected: ((Int)->Unit)={
        viewModel.saveSelectedBefore(it)
    }


}