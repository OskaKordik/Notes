package com.study.noteskotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.study.noteskotlin.models.Note
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.note_toolbar.*

class NoteActivity : AppCompatActivity() {

    // UI components
    private lateinit var mLinedEditText: LinedEditText
    private lateinit var mEditTitle: EditText
    private lateinit var mViewTitle: TextView

    // vars
    private var mIsNewNote = false
    private lateinit var mNoteInitial: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        mLinedEditText = edit_text_note
        mEditTitle = noteEditTitle
        mViewTitle = noteTextTitle

        if (getIncomingIntent()) {
            // this is a new note (EDIT MODE)
            setNewNoteProperties()
        } else {
            // this is note a new note (VIEW MODE)
            setNoteProperties()
        }
    }

    private fun getIncomingIntent() : Boolean {
        if (intent.hasExtra("selected_note")) {
            mNoteInitial = intent.getParcelableExtra<Note>("selected_note")
            mIsNewNote = false
            return false
        }
        mIsNewNote = true
        return true
    }

    private fun setNewNoteProperties() {
        mViewTitle.text = "Note title"
        mEditTitle.setText("Note title")
    }

    private fun setNoteProperties() {
        mViewTitle.text = mNoteInitial.title
        mEditTitle.setText(mNoteInitial.title)
        mLinedEditText.setText(mNoteInitial.content)
    }

}
