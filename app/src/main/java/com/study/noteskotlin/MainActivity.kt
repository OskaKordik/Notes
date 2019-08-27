package com.study.noteskotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.noteskotlin.adapters.RecyclerAdapter
import com.study.noteskotlin.models.Note

class MainActivity : AppCompatActivity() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mViewAdapter: RecyclerView.Adapter<*>
    private lateinit var mViewManager: RecyclerView.LayoutManager
    private val mNotes: ArrayList<Note> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecycleView()
        addNotes()
    }

    private fun addNotes() {
        for (x in 0 until 10) mNotes.add(Note("title $x", "content $x", "00:0$x"))
        mViewAdapter.notifyDataSetChanged()
    }

    private fun initRecycleView() {
        mViewManager = LinearLayoutManager(this)
        mViewAdapter = RecyclerAdapter(mNotes)
        mRecyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = mViewManager
            adapter = mViewAdapter
        }
    }
}
