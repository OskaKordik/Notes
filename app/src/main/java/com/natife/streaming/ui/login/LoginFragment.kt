package com.natife.streaming.ui.login

import android.os.Bundle
import android.os.PatternMatcher
import android.text.Editable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.Patterns
import android.view.View
import androidx.core.view.isVisible
import com.natife.streaming.BuildConfig
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*
import timber.log.Timber

class LoginFragment : BaseFragment<LoginViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (BuildConfig.DEBUG) {
            emailText.setText("tv2@test.com")
            passwordText.setText("tv2")
        }

        emailText.addTextChangedListener(textWatcher)
        passwordText.addTextChangedListener(textWatcher)
        buttonLogin.setOnClickListener {
            viewModel.login(email = emailText.text?.trim().toString(),
                password = passwordText.text?.trim().toString(),
                onError = {
                    emailUnderline.setBackgroundResource(R.color.light_red)
                    passwordUnderline.setBackgroundResource(R.color.light_red)
                    errorText.text = it
                    errorText.isVisible = true
                })
        }

        keyboardView.attachInput(emailTextInput, passwordTextInput)

        goToSiteText.movementMethod = LinkMovementMethod.getInstance()
    }


    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            emailUnderline.setBackgroundResource(R.color.white)
            passwordUnderline.setBackgroundResource(R.color.white)
            errorText.isVisible = false
            emailText.error = null
            passwordText.error = null
            buttonLogin.isEnabled =
                !emailText.text?.trim().isNullOrEmpty()
                        && !passwordText.text?.trim().isNullOrEmpty()
                        && Patterns.EMAIL_ADDRESS.matcher(emailText.text.toString()).matches()
        }
    }

}