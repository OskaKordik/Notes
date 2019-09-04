package com.study.noteskotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.study.noteskotlin.models.Note

class NoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        val note = intent.getParcelableExtra<Note>("selected_note")
        Toast.makeText(this, "note ${note.title}", Toast.LENGTH_SHORT).show()
    }
}
