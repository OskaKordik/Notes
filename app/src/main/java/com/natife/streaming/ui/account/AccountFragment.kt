package com.natife.streaming.ui.account

import android.os.Bundle
import android.view.View
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment: BaseFragment<AccountViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_account

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribe(viewModel.nameLiveData,name::setText)
        subscribe(viewModel.emailLiveData,email::setText)

        buttonLogout.setOnClickListener {
            viewModel.logout()
        }

        backButton.setOnClickListener {
            viewModel.back()
        }

    }
}