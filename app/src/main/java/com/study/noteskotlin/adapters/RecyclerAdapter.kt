package com.study.noteskotlin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.noteskotlin.R
import com.study.noteskotlin.models.Note
import kotlinx.android.synthetic.main.note_list_item.view.*

class RecyclerAdapter(private val notes : ArrayList<Note>) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    var onItemClick: ((Note) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = notes[position].title
        holder.time.text = notes[position].time
    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // !! - null check, title & time is not null and if it’s null -> throw a null pointer exception
        val title = view.noteTitleView!!
        val time = view.timeView!!

        init {
            view.setOnClickListener { onItemClick?.invoke(notes[adapterPosition]) }
        }
    }
}