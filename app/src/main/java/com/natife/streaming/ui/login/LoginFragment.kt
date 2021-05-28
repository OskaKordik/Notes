package com.natife.streaming.ui.login

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.natife.streaming.BuildConfig
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login_new.*


class LoginFragment : BaseFragment<LoginViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_login_new

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        (activity as MainActivity).logo.isVisible = false
        if (BuildConfig.DEBUG) {
            loginTextField.editText?.setText("tv2@test.com")
            passwordTextField.editText?.setText("tv2")
        }

//        loginTextField.addTextChangedListener(textWatcher)
//        passwordText.addTextChangedListener(textWatcher)
        loginTextField.editText?.doOnTextChanged { text, _, _, _ ->
            with(loginTextField) {
                setEndIconDrawable(R.drawable.ic_chack)
                setEndIconTintList(
                    android.content.res.ColorStateList.valueOf(
                        requireActivity().getColor(
                            R.color.blue_500
                        )
                    )
                )
                endIconMode = TextInputLayout.END_ICON_CUSTOM
                if (text != null) {
                    isEndIconVisible =
                        text.isNotEmpty() && text.matches(Patterns.EMAIL_ADDRESS.toRegex())
                }
                isErrorEnabled = false
            }
            loging_button.isEnabled = !text?.trim().isNullOrEmpty()
                    && !passwordTextField.editText?.text?.trim().isNullOrEmpty()
                    && text?.matches(Patterns.EMAIL_ADDRESS.toRegex()) ?: false
        }

        passwordTextField.editText?.doOnTextChanged { text, _, _, _ ->
            with(passwordTextField) {
                isErrorEnabled = false
            }
            loging_button.isEnabled = !text?.trim().isNullOrEmpty()
                    && !loginTextField.editText?.text?.trim().isNullOrEmpty()
                    && loginTextField.editText?.text?.matches(Patterns.EMAIL_ADDRESS.toRegex()) ?: false
        }

        loging_button.setOnClickListener {
            viewModel.login(email = loginTextField.editText?.text?.trim().toString(),
                password = passwordTextField.editText?.text?.trim().toString(),
                onError = {
//                    emailUnderline.setBackgroundResource(R.color.light_red)
//                    passwordUnderline.setBackgroundResource(R.color.light_red)
//                    errorText.text = it
//                    errorText.isVisible = true
                    with(passwordTextField) {
                        error = it
                        isErrorEnabled = true
                    }
                    loging_button.isEnabled = false
                })
        }
        register_button.setOnClickListener {
            viewModel.onRegisterClicked()
        }


//        keyboardView.attachInput(emailTextInput, passwordTextInput)

//      TODO  goToSiteText.movementMethod = LinkMovementMethod.getInstance()
    }


//    private val textWatcher = object : TextWatcher {
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//        }
//
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//        }
//
//        override fun afterTextChanged(s: Editable?) {
//            emailUnderline.setBackgroundResource(R.color.white)
//            passwordUnderline.setBackgroundResource(R.color.white)
//            errorText.isVisible = false
//            emailText.error = null
//            passwordText.error = null
//            buttonLogin.isEnabled =
//                !emailText.text?.trim().isNullOrEmpty()
//                        && !passwordText.text?.trim().isNullOrEmpty()
//                        && Patterns.EMAIL_ADDRESS.matcher(emailText.text.toString()).matches()
//        }
//    }

}