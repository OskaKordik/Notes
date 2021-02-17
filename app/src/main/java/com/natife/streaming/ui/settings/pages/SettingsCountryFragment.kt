package com.natife.streaming.ui.settings.pages

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ui.matchprofile.ActionAdapter
import com.natife.streaming.ui.settings.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_settings_page.*
import kotlinx.android.synthetic.main.fragment_settings_page.title
import timber.log.Timber

class SettingsCountryFragment : BaseFragment<SettingsViewModel>() {
    private val adapter : ActionAdapter by lazy { ActionAdapter(onCheck ={
        viewModel.goToCountryDialog()

    } , onlyClickable = true) }
    override fun getLayoutRes(): Int = R.layout.fragment_settings_page
    override val viewModelLifecycleOwner: LifecycleOwner by lazy { parentFragment ?: this }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        subscribe(viewModel.strings){
            title.text = it.find { it.id == resources.getInteger(R.integer.country)}?.text
            viewModel.getCountry(it.find { it.id == resources.getInteger(R.integer.country)}?.text ?: "")
        }
        subscribe(viewModel.country){
            Timber.e("ijdoijdoifjoidfjfoijfd $it")
            adapter.submitList(it.map { it.copy(name = "${"Язык"} ${it.name}") })
        }



    }


}