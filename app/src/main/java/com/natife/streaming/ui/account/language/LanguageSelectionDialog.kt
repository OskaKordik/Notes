package com.natife.streaming.ui.account.language

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.natife.streaming.R
import com.natife.streaming.base.BaseDialog
import com.natife.streaming.db.entity.LanguageModel
import com.natife.streaming.ext.subscribeEvent
import com.natife.streaming.ui.main.MainActivity
import kotlinx.android.synthetic.main.dialog_language_selection.*

class LanguageSelectionDialog : BaseDialog<LanguageSelectionViewModel>(){

    override fun getLayoutRes(): Int  = R.layout.dialog_language_selection

    private val languageAdapter by lazy {
        LanguageSelectionAdapter{
            onLanguageClicked(it)
        }
    }

    override fun onStart() {
        super.onStart()

        subscribeEvent(viewModel.restart) {
            if (it) {
                val mStartActivity: Intent =
                    Intent(requireContext(), MainActivity::class.java)
                val mPendingIntentId: Int = 123456
                val mPendingIntent: PendingIntent = PendingIntent.getActivity(
                    requireContext(),
                    mPendingIntentId,
                    mStartActivity,
                    PendingIntent.FLAG_CANCEL_CURRENT
                )
                requireActivity().finish()
                requireActivity().startActivity(mStartActivity)
            }
            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_App_Dialog_FullScreen)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvLanguages.adapter = languageAdapter

        languageAdapter.submitList(viewModel.languages)
    }

    private fun onLanguageClicked(language: LanguageModel) {
            viewModel.setLang(language)
    }
}