package com.natife.streaming.ui.login

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.dp
import com.natife.streaming.ext.hideKeyboard
import com.natife.streaming.ext.showKeyboard
import kotlinx.android.synthetic.main.fragment_login_new.*


class LoginFragment : BaseFragment<LoginViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_login_new

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if (BuildConfig.DEBUG) {
//            loginTextField.editText?.setText("tv2@test.com")
//            passwordTextField.editText?.setText("tv2")
//        }

        loginTextField.editText?.doOnTextChanged { text, _, _, count ->
            with(loginTextField) {
                setEndIconDrawable(R.drawable.ic_done)
                setEndIconTintList(
                    android.content.res.ColorStateList.valueOf(
                        requireActivity().getColor(
                            R.color.hint_text_field_color
                        )
                    )
                )
                endIconMode = TextInputLayout.END_ICON_CUSTOM
                if (text != null) {
                    isEndIconVisible =
                        text.isNotEmpty() && text.matches(Patterns.EMAIL_ADDRESS.toRegex())
                }
                hint = if (count > 0) null else context.resources.getString(R.string.enter_login)
                isErrorEnabled = false
            }
            loging_button.isEnabled = !text?.trim().isNullOrEmpty()
                    && !passwordTextField.editText?.text?.trim().isNullOrEmpty()
                    && text?.matches(Patterns.EMAIL_ADDRESS.toRegex()) ?: false
        }

        loginTextField.editText?.onFocusChangeListener =
            View.OnFocusChangeListener { editText, hasFocus ->
                if (hasFocus) {
                    editText.showKeyboard()
                    with(loginTextField.layoutParams) {
                        width = 346.dp
                        loginTextField.layoutParams = this
                    }
                } else {
                    editText.hideKeyboard()
                    with(loginTextField.layoutParams) {
                        width = 305.dp
                        loginTextField.layoutParams = this
                    }
                }
            }

        passwordTextField.editText?.doOnTextChanged { text, _, _, count ->

            text_input_error.text =
                if (count > 0) null else resources.getString(R.string.enter_password)

            with(passwordTextField) {
                isErrorEnabled = false
                hint = if (count > 0) {
                    null
                } else context.resources.getString(R.string.enter_password)
            }
            loging_button.isEnabled = !text?.trim().isNullOrEmpty()
                    && !loginTextField.editText?.text?.trim().isNullOrEmpty()
                    && loginTextField.editText?.text?.matches(Patterns.EMAIL_ADDRESS.toRegex()) ?: false
        }

        // Если к компоненту перешёл фокус
        passwordTextField.editText?.onFocusChangeListener =
            View.OnFocusChangeListener { editText, hasFocus ->
                if (hasFocus) {
                    editText.showKeyboard()
                    with(passwordTextField.layoutParams) {
                        width = 346.dp
                        passwordTextField.layoutParams = this
                    }
                } else {
                    editText.hideKeyboard()
                    with(passwordTextField.layoutParams) {
                        width = 305.dp
                        passwordTextField.layoutParams = this
                    }
                }
            }

        loging_button.setOnClickListener {
            viewModel.login(
                email = loginTextField.editText?.text?.trim().toString(),
                password = passwordTextField.editText?.text?.trim().toString(),
                onError = {
                    with(text_input_error) {
                        text = it
                    }
                    loging_button.isEnabled = false
                    passwordTextField.requestFocus()
                })
        }

        register_button.setOnClickListener {
            viewModel.onRegisterClicked()
        }

    }
}