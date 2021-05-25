package com.natife.streaming.ui.home.tournament

import android.os.Bundle
import android.view.View
import com.natife.streaming.base.impl.VariantDialog
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.dialog_variant.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

@Deprecated("don'tuse")
class TournamentDialog: VariantDialog<TournamentDialogViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogTitle.text = "Выберите турнир"//TODO multilang
        subscribe(viewModel.list){

            adapter.submitList(it.map { it.name })
        }
        adapter.onSelect={
            viewModel.select(it)
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(TournamentDialogArgs.fromBundle(requireArguments()))
    }
}