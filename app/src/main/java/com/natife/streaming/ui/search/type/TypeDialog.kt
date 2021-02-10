package com.natife.streaming.ui.search.type

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.natife.streaming.base.BaseDialog
import com.natife.streaming.base.impl.VariantDialog
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.dialog_variant.*

class TypeDialog: VariantDialog<TypeDialogViewModel>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogTitle.isVisible = false
        subscribe(viewModel.list,adapter::submitList)

        adapter.onSelect={
            viewModel.select(it)
        }
    }
}