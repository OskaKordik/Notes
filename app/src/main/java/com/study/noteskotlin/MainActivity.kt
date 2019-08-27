package com.study.noteskotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.study.noteskotlin.models.Note

class MainActivity : AppCompatActivity() {

    private val TAG = "NotesListActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val note = Note()
        Log.d(TAG, "onCreate: $note")

        val note2 = Note("myTitle", "myContent", "myTime")
        Log.d(TAG, "onCreate: $note2")
    }
}
