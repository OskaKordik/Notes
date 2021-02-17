package com.natife.streaming.ui.settings.pages.countrydialog

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.natife.streaming.base.impl.VariantDialog
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ui.settings.SettingsViewModel
import kotlinx.android.synthetic.main.dialog_variant.*
import timber.log.Timber
import java.util.*

class CountryDialog: VariantDialog<SettingsViewModel>() {
    override val viewModelLifecycleOwner: LifecycleOwner by lazy { parentFragment ?: this }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            dialogTitle.text = "Выберите страну"//todo need lexic

        val list = Locale.getAvailableLocales()
        val prepared = list.filter { it.getDisplayCountry(it).isNotEmpty()}
        adapter.submitList(prepared.map {
            it.getDisplayCountry(it)
        })
        adapter.onSelect={
            Timber.e("KDMLKDMDK ${prepared[it].country}")
            viewModel.saveCountry(prepared[it])
        }
    }
}