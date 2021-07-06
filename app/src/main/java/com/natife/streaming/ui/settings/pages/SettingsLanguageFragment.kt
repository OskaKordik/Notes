package com.natife.streaming.ui.settings.pages

//import android.os.Bundle
//import android.view.View
//import androidx.lifecycle.LifecycleOwner
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.natife.streaming.R
//import com.natife.streaming.base.BaseFragment
//import com.natife.streaming.ext.subscribe
//import com.natife.streaming.ui.matchprofile.ActionAdapter
//import com.natife.streaming.ui.settings.SettingsViewModel
//import kotlinx.android.synthetic.main.fragment_settings_page.*
//
//class SettingsLanguageFragment : BaseFragment<SettingsViewModel>() {
//    private val adapter : ActionAdapter by lazy { ActionAdapter(onCheck ={
//        viewModel.goToLanguageDialog()
//
//    } , onlyClickable = true) }
//    override fun getLayoutRes(): Int = R.layout.fragment_settings_page
//    override val viewModelLifecycleOwner: LifecycleOwner by lazy { parentFragment ?: this }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        recycler.layoutManager = LinearLayoutManager(context)
//        recycler.adapter = adapter
//
//        subscribe(viewModel.strings){
//            title.text = "Язык"//TODO need lexic
//            viewModel.getLanguage("Язык")//TODO need lexic
//            subscribe(viewModel.language){
//                adapter.submitList(it.map { it.copy(name = "${"Язык"} ${it.name}") })
//            }
//        }
//
//
//    }
//}