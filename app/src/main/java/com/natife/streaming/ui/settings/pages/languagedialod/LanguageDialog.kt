package com.natife.streaming.ui.settings.pages.languagedialod

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.natife.streaming.R
import com.natife.streaming.base.impl.VariantDialog
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ui.settings.SettingsViewModel
import kotlinx.android.synthetic.main.dialog_variant.*
import kotlinx.android.synthetic.main.fragment_settings_page.*
import java.util.*

class LanguageDialog: VariantDialog<SettingsViewModel>() {
    override val viewModelLifecycleOwner: LifecycleOwner by lazy { parentFragment ?: this }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getStrings()
        subscribe(viewModel.strings){
            dialogTitle.text = it.find { it.id == resources.getInteger(R.integer.selection_language)}?.text
        }
            val list = listOf<Locale>(Locale("ru"),Locale("en"))
            adapter.submitList(list.map {
                it.getDisplayLanguage(it)
            })
            adapter.onSelect = {
                viewModel.saveLanguage(list[it])
            }

    }

}