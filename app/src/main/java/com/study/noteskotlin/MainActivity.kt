package com.study.noteskotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.noteskotlin.adapters.RecyclerAdapter
import com.study.noteskotlin.models.Note
import com.study.noteskotlin.utils.VerticalSpacingItemDecorator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mViewAdapter: RecyclerView.Adapter<*>
    private val mNotes: ArrayList<Note> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecycleView()
        addNotes()

        setSupportActionBar(findViewById(R.id.notesToolbar))
        title = getString(R.string.app_name)
    }

    private fun addNotes() {
        for (x in 0 until 10) mNotes.add(Note("title $x", "content $x", "00:0$x"))
        mViewAdapter.notifyDataSetChanged()
    }

    private fun initRecycleView() {
        mViewAdapter = RecyclerAdapter(mNotes)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mViewAdapter
            addItemDecoration(VerticalSpacingItemDecorator(5))
        }
    }
}
