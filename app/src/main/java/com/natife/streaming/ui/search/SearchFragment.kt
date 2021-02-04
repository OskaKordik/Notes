package com.natife.streaming.ui.search

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.KeyEvent
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.doOnResult
import com.natife.streaming.ext.showToast
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ext.withAllPermissions
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.keyboard.view.*
import kotlinx.android.synthetic.main.view_interval.*
import timber.log.Timber
import java.util.*

class SearchFragment : BaseFragment<SearchViewModel>() {

    private val adapter: SearchAdapter by lazy { SearchAdapter() }
    private val speechRecognizer: SpeechRecognizer by lazy {
        SpeechRecognizer.createSpeechRecognizer(
            requireContext()
        )
    }
    private val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        //putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale("RU"))
    }


    override fun getLayoutRes(): Int = R.layout.fragment_search

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchRecycler.layoutManager = LinearLayoutManager(context)
        searchRecycler.adapter = adapter
        adapter.onClick = {
            viewModel.select(it)
        }


        subscribe(viewModel.results, adapter::submitList)
        subscribe(viewModel.genderLiveDate,buttonGender::setText)
        subscribe(viewModel.sportLiveDate,buttonSport::setText)
        subscribe(viewModel.typeLiveDate,buttonType::setText)


        keyboardView2.attachInput(searchInput)
        keyboardView2.enableSearch(true)
        keyboardView2.searchBtn.setOnClickListener {
            searchInput.text?.let {
                if (it.isNotEmpty()) {
                    viewModel.search(it.toString())
                }
            }
        }
        searchInput.requestFocus()
        searchInput.doAfterTextChanged {
            it?.let {
                    viewModel.searchLocal(it.toString())
            }

        }


        var lastAction = KeyEvent.ACTION_UP
        voiceInput.setOnKeyListener { _, keyCode, event ->
            Timber.e("$event")
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                context?.withAllPermissions(Manifest.permission.RECORD_AUDIO) {
                    when (event.action) {
                        KeyEvent.ACTION_DOWN -> {
                            if (lastAction != KeyEvent.ACTION_DOWN) {
                                speechRecognizer.startListening(speechRecognizerIntent)
                                lastAction = KeyEvent.ACTION_DOWN
                            }
                        }
                        KeyEvent.ACTION_UP -> {
                            //speechRecognizer.stopListening()
                            lastAction = KeyEvent.ACTION_UP
                        }
                    }
                }

            }
            return@setOnKeyListener false
        }

        speechRecognizer.doOnResult({
            searchInput.setText(it)
        }, {
            speechProgress.isVisible = true
        }, {
            speechProgress.isVisible = false
        }, {
            showToast("Can not recognize")
        })

        buttonType.setOnClickListener {
            viewModel.showTypeDialog()
        }
        buttonGender.setOnClickListener {
            viewModel.showGenderDialog()
        }
        buttonSport.setOnClickListener {
            viewModel.showSportDialog()
        }
        backButton.setOnClickListener {
            viewModel.back()
        }

    }

    override fun onDestroy() {
        speechRecognizer.stopListening()
        super.onDestroy()
    }


}