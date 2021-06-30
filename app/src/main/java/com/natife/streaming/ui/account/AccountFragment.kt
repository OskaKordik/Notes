package com.natife.streaming.ui.account

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.content.Context
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.db.entity.Lang
import com.natife.streaming.ext.predominantColorToGradient
import kotlinx.android.synthetic.main.fragment_account.*
import timber.log.Timber


class AccountFragment : BaseFragment<AccountViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_account

    private val listener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            viewModel.setLang(position)
            Timber.e(position.toString())
            if ((viewModel.lastposition != null)&&(viewModel.lastposition != position)) {
                val mStartActivity: Intent = Intent(requireContext(), com.natife.streaming.ui.main.MainActivity::class.java)
                val mPendingIntentId: Int = 123456
                val mPendingIntent: PendingIntent = PendingIntent.getActivity(
                    requireContext(),
                    mPendingIntentId,
                    mStartActivity,
                    PendingIntent.FLAG_CANCEL_CURRENT
                )
                val mgr: AlarmManager =
                    requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 200, mPendingIntent)
                System.exit(0)
            }
            viewModel.lastposition = position
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_subscriptions.apply {
            this.requestFocus()
            isSelected = true
        }

    }

    override fun onStart() {
        super.onStart()

        requireActivity().findViewById<Group>(R.id.main_group).visibility = View.GONE
        requireActivity().findViewById<Group>(R.id.main_background_group).visibility = View.GONE
        requireActivity().findViewById<Group>(R.id.profile_background_group).visibility =
            View.VISIBLE
        requireActivity().findViewById<MotionLayout>(R.id.mainMotion)
            .predominantColorToGradient("#CB312A")


        buttonLogout.setOnClickListener {

            val alert = AlertDialog.Builder(requireContext()).apply {
                setTitle(getString(R.string.text_close_app_title))
                setMessage(getString(R.string.text_logout))

                setPositiveButton(getString(R.string.text_yes)) { _, _ ->
                    viewModel.logout()
                }

                setNegativeButton(getString(R.string.text_no)) { _, _ ->
                }
                setCancelable(true)
            }.create()
            alert.show()
            val buttonPos = alert.getButton(DialogInterface.BUTTON_POSITIVE)
            buttonPos.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    buttonPos.setBackgroundColor(resources.getColor(R.color.gray_400))
                } else {
                    buttonPos.setBackgroundColor(resources.getColor(R.color.white))
                }
            }
            buttonPos.setTextColor(resources.getColor(R.color.black))
            buttonPos.setBackgroundColor(resources.getColor(R.color.white))
            val buttonNeg = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
            buttonNeg.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    buttonNeg.setBackgroundColor(resources.getColor(R.color.gray_400))
                } else {
                    buttonNeg.setBackgroundColor(resources.getColor(R.color.white))
                }
            }
            buttonNeg.requestFocus()
            buttonNeg.setTextColor(resources.getColor(R.color.black))
            buttonNeg.setBackgroundColor(resources.getColor(R.color.gray_400))
        }

        viewModel.initialization(resources.getString(R.string.lang))

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.languages,
            R.layout.item_spinner_lang,
        )

        text_language.adapter = adapter
        text_language.onItemSelectedListener = listener

        button_subscriptions.setOnClickListener { viewModel.toSubscriptions() }
        button_pay_story.setOnClickListener { viewModel.toPayStory() }
        button_language.setOnClickListener {
            text_language.performClick()
        }

        button_score_visible.setOnClickListener {
            viewModel.setScore()
            val button =
                requireActivity().findViewById<com.google.android.material.button.MaterialButton>(R.id.score_button)
            if (viewModel.settings.value!!.showScore) {
                button.isChecked = false
                button.text = resources.getString(R.string.showe_account)
            } else {
                button.isChecked = true
                button.text = resources.getString(R.string.hide_account)
            }
        }


        subscribeLiveData()

    }

    @SuppressLint("SetTextI18n")
    private fun subscribeLiveData() {
        viewModel.profileLiveData.observe(viewLifecycleOwner, { profile ->
            requireActivity().findViewById<TextView>(R.id.profile_name).text =
                "${profile.lastName} ${profile.firstName}"
            text_phone.text = "${getString(R.string.text_phone)} ${profile.phone}"
            text_country.text =
                "${getString(R.string.text_country)} ${profile.country?.nameEng ?: ""}"
            email.text = "${getString(R.string.text_email)} ${profile.email}"
        })

        viewModel.settings.observe(viewModelLifecycleOwner, { setting ->
            if (!setting.showScore) text_visible_score.text = getText(R.string.text_no)
            else text_visible_score.text = getText(R.string.text_yes)
            if (setting.lang == Lang.EN) {
                text_language.setSelection(0)
            } else {
                text_language.setSelection(1)
            }
        })
        viewModel.loadersLiveData.observe(viewLifecycleOwner) {
            progressBar_account.isVisible = it
            profile_layout.isVisible = !it
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().findViewById<Group>(R.id.main_group).visibility = View.VISIBLE
        requireActivity().findViewById<Group>(R.id.main_background_group).visibility = View.VISIBLE
        requireActivity().findViewById<MotionLayout>(R.id.mainMotion)
            .predominantColorToGradient("#3560E1")
        requireActivity().findViewById<Group>(R.id.profile_background_group).visibility = View.GONE
    }

}