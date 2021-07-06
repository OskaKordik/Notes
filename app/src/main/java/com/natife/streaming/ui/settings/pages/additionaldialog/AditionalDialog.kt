package com.natife.streaming.ui.settings.pages.additionaldialog

//import android.os.Bundle
//import android.view.View
//import androidx.lifecycle.LifecycleOwner
//import androidx.navigation.fragment.navArgs
//import com.natife.streaming.base.impl.VariantAdapter
//import com.natife.streaming.base.impl.VariantDialog
//import com.natife.streaming.ext.subscribe
//import com.natife.streaming.ui.settings.SettingsViewModel
//import kotlinx.android.synthetic.main.dialog_variant.*

//class AditionalDialog: VariantDialog<SettingsViewModel>() {
//    override val viewModelLifecycleOwner: LifecycleOwner by lazy { parentFragment ?: this }
//    private val arguments: AditionalDialogArgs by navArgs()
//    private val subAdapter: SubscriptionAdapter by lazy { SubscriptionAdapter() }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//            dialogTitle.text = "Выберите доп. подписки"//todo need lexic
//
//        dialogRecycler.adapter=subAdapter
//        subAdapter.submitList(arguments.subscriptions.toList())
//
//    }
//
//}