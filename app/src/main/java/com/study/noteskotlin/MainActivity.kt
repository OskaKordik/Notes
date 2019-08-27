package com.study.noteskotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.noteskotlin.adapters.RecyclerAdapter
import com.study.noteskotlin.models.Note

class MainActivity : AppCompatActivity() {

//    private val TAG = "NotesListActivity"
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val notes: ArrayList<Note> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addNotes()

        viewManager = LinearLayoutManager(this)
        viewAdapter = RecyclerAdapter(notes)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

//        Log.d(TAG, "onCreate: $note2")
    }

    private fun addNotes() {
        notes.add(Note("title 1", "content 1", "00:01"))
        notes.add(Note("title 2", "content 2", "00:02"))
        notes.add(Note("title 3", "content 3", "00:03"))
        notes.add(Note("title 4", "content 4", "00:04"))
        notes.add(Note("title 5", "content 5", "00:05"))
        notes.add(Note("title 6", "content 6", "00:06"))
        notes.add(Note("title 7", "content 7", "00:07"))
    }
}
