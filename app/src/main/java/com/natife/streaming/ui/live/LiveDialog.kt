package com.natife.streaming.ui.live

import android.os.Bundle
import com.natife.streaming.R
import com.natife.streaming.base.BaseDialog
import com.natife.streaming.base.EmptyViewModel

class LiveDialog : BaseDialog<EmptyViewModel>() {
    override fun getLayoutRes(): Int = R.layout.dialog_live

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_App_Dialog_FullScreen)
    }
}