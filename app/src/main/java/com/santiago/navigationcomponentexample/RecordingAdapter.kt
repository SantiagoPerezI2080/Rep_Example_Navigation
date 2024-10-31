package com.santiago.navigationcomponentexample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import com.santiago.navigationcomponentexample.R


class RecordingAdapter(
    private val recordings: List<File>,
    private val onItemClick: (File) -> Unit
) : RecyclerView.Adapter<RecordingAdapter.RecordingViewHolder>() {

    class RecordingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fileName: TextView = view.findViewById(R.id.tvFileName)
        val playButton: Button = view.findViewById(R.id.btnPlay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recording, parent, false)
        return RecordingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordingViewHolder, position: Int) {
        val recording = recordings[position]
        holder.fileName.text = recording.name
        holder.playButton.setOnClickListener { onItemClick(recording) }
    }

    override fun getItemCount(): Int = recordings.size
}
