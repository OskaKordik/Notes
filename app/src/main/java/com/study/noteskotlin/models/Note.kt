package com.study.noteskotlin.models

class Note {
    private var title: String
    private var content: String
    private var time: String

    constructor(title: String = "title1", content: String = "content1", time: String = "time1") {
        this.title = title
        this.content = content
        this.time = time
    }

    override fun toString(): String {
        return "Note(title='$title', content='$content', time='$time')"
    }

}