package com.study.noteskotlin.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(
    var title: String = "title",
    var content: String = "content",
    var time: String = "time")  : Parcelable {


    override fun toString(): String {
        return "Note(title='$title', content='$content', time='$time')"
    }
}