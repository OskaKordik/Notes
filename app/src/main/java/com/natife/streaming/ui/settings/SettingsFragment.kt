package com.natife.streaming.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioGroup
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ui.settings.pages.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment<SettingsViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_settings

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getStrings()
        childFragmentManager.beginTransaction().replace(settingsContainer.id,SettingsSportFragment()).commit()
        settingsRadio.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                sport.id -> childFragmentManager.beginTransaction().replace(settingsContainer.id,SettingsSportFragment()).commit()
                payment.id -> childFragmentManager.beginTransaction().replace(settingsContainer.id,SettingsPaymentFragment()).commit()
                subscription.id -> childFragmentManager.beginTransaction().replace(settingsContainer.id,SettingsSubscriptionFragment()).commit()
                additionalSubscription.id -> childFragmentManager.beginTransaction().replace(settingsContainer.id,SettingsAddSubscriptionFragment()).commit()
                language.id -> childFragmentManager.beginTransaction().replace(settingsContainer.id,SettingsLanguageFragment()).commit()
                country.id -> childFragmentManager.beginTransaction().replace(settingsContainer.id,SettingsCountryFragment()).commit()
            }
        }


    }
}