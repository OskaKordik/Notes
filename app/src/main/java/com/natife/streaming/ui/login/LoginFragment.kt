package com.natife.streaming.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment<LoginViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailText.addTextChangedListener(textWatcher)
        passwordText.addTextChangedListener(textWatcher)
        buttonLogin.setOnClickListener {
            viewModel.login(email = emailText.text?.trim().toString(),
                password = passwordText.text?.trim().toString(),
                onError = {
                    emailText.error = it
                    passwordText.error = it
                })
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            emailText.error = null
            passwordText.error = null
            buttonLogin.isEnabled =
                !emailText.text?.trim().isNullOrEmpty() && !passwordText.text?.trim()
                    .isNullOrEmpty()
        }
    }
}