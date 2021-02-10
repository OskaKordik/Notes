package com.natife.streaming.ui.home.score

import android.os.Bundle
import android.view.View
import com.natife.streaming.base.impl.VariantDialog
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.dialog_variant.*

class ScoreDialog : VariantDialog<ScoreViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribe(viewModel.showScore, adapter::submitList)
        subscribe(viewModel.title, dialogTitle::setText)

        adapter.onSelect = { position ->
            viewModel.select(position)
        }
    }
}