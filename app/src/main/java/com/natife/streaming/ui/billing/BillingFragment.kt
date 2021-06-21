package com.natife.streaming.ui.billing

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.predominantColorToGradient
import kotlinx.android.synthetic.main.fragment_billing.*
import kotlinx.android.synthetic.main.fragment_mypreferences_new.topConstraintLayout
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class BillingFragment : BaseFragment<BillingViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_billing
    private lateinit var billingType: Array<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Heading in the predominant team color
        topConstraintLayout.predominantColorToGradient("#CB312A")

        billingType = resources.getStringArray(R.array.billing_type_names)
        val billingAdapter = BillingFragmentAdapter(
            childFragmentManager,
            this.lifecycle, billingType.size
        )
        billing_pager.adapter = billingAdapter
        TabLayoutMediator(tab_billing_layout, billing_pager) { tab, position ->
            tab.text = billingType[position]
        }.attach()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.onFinishClicked()
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(BillingFragmentArgs.fromBundle(requireArguments()))
    }

    inner class BillingFragmentAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        private val itemCount: Int
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int {
            return itemCount
        }

        override fun createFragment(position: Int): Fragment {
            return Fragment()
        }

    }
}