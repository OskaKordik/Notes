package com.natife.streaming.ui.register

import android.os.Bundle
import android.view.View
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_register_new.*


class RegisterFragment : BaseFragment<RegisterViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_register_new

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerButton.setOnClickListener {
            viewModel.userRegistration(email = "tv2@test.com", password = "tv2",onError = {})
        }
    }
}