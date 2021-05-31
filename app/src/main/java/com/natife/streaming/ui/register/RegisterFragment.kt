package com.natife.streaming.ui.register

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.dp
import com.natife.streaming.ext.hideKeyboard
import kotlinx.android.synthetic.main.fragment_register_new.*


class RegisterFragment : BaseFragment<RegisterViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_register_new

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginTextField.editText?.requestFocus()

        loginTextField.editText?.doOnTextChanged { text, _, _, count ->
            with(loginTextField) {
//                setEndIconDrawable(R.drawable.ic_done)
//                setEndIconTintList(
//                    android.content.res.ColorStateList.valueOf(
//                        requireActivity().getColor(
//                            R.color.hint_text_field_color
//                        )
//                    )
//                )
                endIconMode = TextInputLayout.END_ICON_CUSTOM
                if (text != null) {
                    isEndIconVisible =
                        text.isNotEmpty() && text.matches(Patterns.EMAIL_ADDRESS.toRegex())
                }
                hint = if (count > 0) null else context.resources.getString(R.string.enter_login)
                isErrorEnabled = false
            }
            registerButton.isEnabled = !text?.trim().isNullOrEmpty()
                    && !passwordTextField.editText?.text?.trim().isNullOrEmpty()
                    && text?.matches(Patterns.EMAIL_ADDRESS.toRegex()) ?: false
        }

        loginTextField.editText?.onFocusChangeListener =
            View.OnFocusChangeListener { editText, hasFocus ->
                if (hasFocus) {
//                    editText.showKeyboard()
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
                if (count > 0) {
                    hint = null
                }
            }
            registerButton.isEnabled = !text?.trim().isNullOrEmpty()
                    && !loginTextField.editText?.text?.trim().isNullOrEmpty()
                    && loginTextField.editText?.text?.matches(Patterns.EMAIL_ADDRESS.toRegex()) ?: false
        }

        // Если к компоненту перешёл фокус
        passwordTextField.editText?.onFocusChangeListener =
            View.OnFocusChangeListener { editText, hasFocus ->
                if (hasFocus) {
//                    editText.showKeyboard()
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

        registerButton.setOnClickListener {
            viewModel.userRegistration(
                lang = resources.getString(R.string.lang),
                email = loginTextField.editText?.text?.trim().toString(),
                password = passwordTextField.editText?.text?.trim().toString(),
                onError = {
                    with(text_input_error) {
                        text = it
                    }
                    registerButton.isEnabled = false
                    passwordTextField.requestFocus()
                })
        }
//        registerButton.setOnClickListener {
//            viewModel.userRegistration(
//                lang = resources.getString(R.string.lang),
//                email = "youdin.alexey.natife@gmail.com",
//                password = "fghjYdJty4573",
//                onError = {})
//        }
    }
}