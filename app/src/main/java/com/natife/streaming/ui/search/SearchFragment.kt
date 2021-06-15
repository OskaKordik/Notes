package com.natife.streaming.ui.search

import android.os.Bundle
import android.view.View
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.predominantColorToGradient
import com.natife.streaming.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*


class SearchFragment : BaseFragment<SearchViewModel>() {

    private val adapter: SearchAdapter by lazy { SearchAdapter() }
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

    override fun getLayoutRes(): Int = R.layout.fragment_search_new

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
    }

//    override fun onDestroy() {
//        speechRecognizer.stopListening()
//
//        super.onDestroy()
//    }


}