package com.natife.streaming.ui.home.live

import android.os.Bundle
import android.view.View
import com.natife.streaming.base.impl.VariantDialog
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.dialog_variant.*

@Deprecated("don'tuse")
class LiveDialog: VariantDialog<LiveViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribe(viewModel.list,adapter::submitList)
        dialogTitle.text = "Live"//TODO multilang
        adapter.onSelect ={
            viewModel.saveType(it)
        }
        adapter.onSelect={
            viewModel.onSelect(it)
        }
    }
}