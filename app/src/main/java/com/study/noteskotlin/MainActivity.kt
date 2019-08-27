package com.study.noteskotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.noteskotlin.adapters.RecyclerAdapter
import com.study.noteskotlin.models.Note

class MainActivity : AppCompatActivity() {

//    private val TAG = "NotesListActivity"
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mViewAdapter: RecyclerView.Adapter<*>
    private lateinit var mViewManager: RecyclerView.LayoutManager
    private val mNotes: ArrayList<Note> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addNotes()

        mViewManager = LinearLayoutManager(this)
        mViewAdapter = RecyclerAdapter(mNotes)
        mRecyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = mViewManager
            adapter = mViewAdapter
        }

//        Log.d(TAG, "onCreate: $note2")
    }

    private fun addNotes() {
        mNotes.add(Note("title 1", "content 1", "00:01"))
        mNotes.add(Note("title 2", "content 2", "00:02"))
        mNotes.add(Note("title 3", "content 3", "00:03"))
        mNotes.add(Note("title 4", "content 4", "00:04"))
        mNotes.add(Note("title 5", "content 5", "00:05"))
        mNotes.add(Note("title 6", "content 6", "00:06"))
        mNotes.add(Note("title 7", "content 7", "00:07"))
    }
}
