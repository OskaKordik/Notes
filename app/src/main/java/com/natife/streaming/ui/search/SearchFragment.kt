package com.natife.streaming.ui.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.navigation.navGraphViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.hideKeyboard
import com.natife.streaming.ext.predominantColorToGradient
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_popup_video.*
import kotlinx.android.synthetic.main.fragment_search_new.*
import kotlinx.android.synthetic.main.fragment_settings_page.*


class SearchFragment : BaseFragment<SearchViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_search_new
    private lateinit var searchTabNames: Array<String>
    private val searchResultViewModel: SearchResultViewModel by navGraphViewModels(R.id.nav_main)
    private var page = 0
    val onPage = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            position.apply {
                page = this
                searchResultViewModel.startViewID.value?.let { start ->
                    search_tab_layout.getTabAt(0)?.view?.nextFocusDownId = start[this]
                    search_tab_layout.getTabAt(1)?.view?.nextFocusDownId = start[this]
                    search_tab_layout.getTabAt(2)?.view?.nextFocusDownId = start[this]
                }

            }
        }
    }

//    private val adapter: SearchAdapter by lazy { SearchAdapter() }
//    private val speechRecognizer: SpeechRecognizer by lazy {
//        SpeechRecognizer.createSpeechRecognizer(
//            requireContext()
//        )
//    }
//    private val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
//        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
//        //putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale("RU"))
//    }

//    private fun applyAlpha(alpha: Float){
////        (activity as MainActivity).logo?.alpha = alpha
//        buttonType?.alpha = 1 - alpha
//        buttonSport?.alpha = 1 - alpha
//        buttonGender?.alpha = 1 - alpha
//    }
//    private val transitionListener = object :
//        MotionLayout.TransitionListener {
//        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
//        }
//
//        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
//            applyAlpha(p3)
//        }
//
//        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
//        }
//
//        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
//        }
//
//    }

//    override fun onResume() {
//        ((activity as MainActivity).mainMotion as MotionLayout).addTransitionListener(transitionListener)
//        super.onResume()
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).main_group?.visibility = View.GONE
        (activity as MainActivity).main_background_group?.visibility = View.GONE
        (activity as MainActivity).search_background_group?.visibility = View.VISIBLE
        //Heading in the predominant team color
        (activity as MainActivity).mainMotion?.predominantColorToGradient("#CB312A")
        searchTabNames = resources.getStringArray(R.array.search_names)
        val searchFragmentAdapter = SearchFragmentAdapter(
            childFragmentManager,
            this.lifecycle,
            searchTabNames.size
        )
        search_pager.adapter = searchFragmentAdapter
        search_pager.registerOnPageChangeCallback(onPage)

        TabLayoutMediator(search_tab_layout, search_pager) { tab, position ->
            tab.text = searchTabNames[position]
        }.attach()




        (activity as MainActivity).search_text_field?.editText?.doOnTextChanged { _, start, _, count ->
            with((activity as MainActivity).search_text_field) {
                hint =
                    if (start + count > 0) null else context.resources.getString(R.string.search)
            }
        }
        (activity as MainActivity).search_text_field?.editText?.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) v.hideKeyboard()
            }
        (activity as MainActivity).search_text_field?.editText?.setOnEditorActionListener(
            OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    v.clearFocus()
                    search_tab_layout.getChildAt(0).requestFocus()
                    search_tab_layout.getChildAt(0).isSelected = true
                    viewModel.search((activity as MainActivity).search_text_field?.editText?.text.toString())
                    return@OnEditorActionListener true
                }
                false
            })

        subscribe(viewModel.resultsTeam, searchResultViewModel::setResultsTeam)
        subscribe(viewModel.resultsPlayer, searchResultViewModel::setResultsPlayer)
        subscribe(viewModel.resultsTournament, searchResultViewModel::setResultsTournament)
        subscribe(searchResultViewModel.startViewID) { start ->
            page.apply {
                search_tab_layout.getTabAt(0)?.view?.nextFocusDownId = start[this]
                search_tab_layout.getTabAt(1)?.view?.nextFocusDownId = start[this]
                search_tab_layout.getTabAt(2)?.view?.nextFocusDownId = start[this]
            }
        }
        subscribe(searchResultViewModel.searchResultClicked, viewModel::select)

//        applyAlpha(((activity as MainActivity).mainMotion as MotionLayout).progress)


//        searchRecycler.layoutManager = LinearLayoutManager(context)
//        searchRecycler.adapter = adapter
//        searchRecycler.isFocusable = false
//        adapter.onClick = {
//            viewModel.select(it)
//        }


//        subscribe(viewModel.results, adapter::submitList)
//        subscribe(viewModel.genderLiveDate,buttonGender::setText)
//        subscribe(viewModel.sportLiveDate,buttonSport::setText)
//        subscribe(viewModel.typeLiveDate,buttonType::setText)


//        keyboardView2.attachInput(searchInput)
//        keyboardView2.enableSearch(true)
//        keyboardView2.searchBtn.setOnClickListener {
//            searchInput.text?.let {
//                if (it.isNotEmpty()) {
//                    viewModel.search(it.toString())
//                }
//            }
//        }
//        searchInput.requestFocus()
//        searchInput.doAfterTextChanged {
//            it?.let {
//                    viewModel.searchLocal(it.toString())
//            }
//
//        }


//        var lastAction = KeyEvent.ACTION_UP
//        voiceInput.setOnKeyListener { _, keyCode, event ->
//            Timber.e("$event")
//            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
//                context?.withAllPermissions(Manifest.permission.RECORD_AUDIO) {
//                    when (event.action) {
//                        KeyEvent.ACTION_DOWN -> {
//                            if (lastAction != KeyEvent.ACTION_DOWN) {
//                                speechRecognizer.startListening(speechRecognizerIntent)
//                                lastAction = KeyEvent.ACTION_DOWN
//                            }
//                        }
//                        KeyEvent.ACTION_UP -> {
//                            //speechRecognizer.stopListening()
//                            lastAction = KeyEvent.ACTION_UP
//                        }
//                    }
//                }
//
//            }
//            return@setOnKeyListener false
//        }
//
//        speechRecognizer.doOnResult({
//            searchInput.setText(it)
//        }, {
//            speechProgress.isVisible = true
//        }, {
//            speechProgress.isVisible = false
//        }, {
//            showToast("Can not recognize")
//        })
//
//        buttonType.setOnClickListener {
//            viewModel.showTypeDialog()
//        }
//        buttonGender.setOnClickListener {
//            viewModel.showGenderDialog()
//        }
//        buttonSport.setOnClickListener {
//            viewModel.showSportDialog()
//        }


    }

    private fun doSearch(text: String) {

    }

//    override fun onPause() {
//        ((activity as MainActivity).mainMotion as MotionLayout).removeTransitionListener(
//            transitionListener
//        )
//        super.onPause()
//    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).main_group?.visibility = View.VISIBLE
        (activity as MainActivity).mainMotion?.predominantColorToGradient("#3560E1")
        (activity as MainActivity).main_background_group?.visibility = View.VISIBLE
        (activity as MainActivity).search_background_group?.visibility = View.GONE
        search_pager.unregisterOnPageChangeCallback(onPage)
    }

    //    override fun onDestroy() {
//        speechRecognizer.stopListening()
//
//        super.onDestroy()
//    }
    inner class SearchFragmentAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        private val itemCount: Int
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int {
            return itemCount
        }

        override fun createFragment(position: Int): Fragment {
            return SearchResultFragment.newInstance(position)
        }
    }
}