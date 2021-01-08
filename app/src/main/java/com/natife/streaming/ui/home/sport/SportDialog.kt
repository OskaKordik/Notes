package com.natife.streaming.ui.home.sport

import android.os.Bundle
import android.view.View
import com.natife.streaming.base.impl.VariantDialog
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.dialog_variant.*

class SportDialog: VariantDialog<SportViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribe(viewModel.sport, ){
            adapter.submitList(it.map { it.name.capitalize() })
        }
        subscribe(viewModel.title, dialogTitle::setText)

        adapter.onSelect = { position ->
            viewModel.select(position)
        }
    }
}