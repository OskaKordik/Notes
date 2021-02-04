package com.natife.streaming.ui.search.sport

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.natife.streaming.base.impl.VariantDialog
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.dialog_variant.*

class SportDialog: VariantDialog<SportViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogTitle.isVisible = false
        subscribe(viewModel.list){
            adapter.submitList( mutableListOf("Любой").apply {
                this.addAll(it.map { it.name.capitalize() })
            })
        }
        adapter.onSelect={
            viewModel.select(it)
        }
    }
}