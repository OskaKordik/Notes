package com.study.noteskotlin.models

class Note(
    var title: String = "title",
    var content: String = "content",
    var time: String = "time") {


    override fun toString(): String {
        return "Note(title='$title', content='$content', time='$time')"
    }
}