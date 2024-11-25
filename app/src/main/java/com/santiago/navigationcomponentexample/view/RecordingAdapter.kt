package com.santiago.navigationcomponentexample.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import com.santiago.navigationcomponentexample.R


// Adaptador para mostrar una lista de grabaciones en un RecyclerView
class RecordingAdapter(
    private val recordings: List<File>, // Lista de archivos de grabaciones
    private val onItemClick: (File) -> Unit // Callback que se ejecuta al hacer clic en el botón de reproducir
) : RecyclerView.Adapter<RecordingAdapter.RecordingViewHolder>() {

    // ViewHolder interno para representar los elementos individuales en el RecyclerView
    class RecordingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Referencias a los elementos de la interfaz de cada grabación
        val fileName: TextView = view.findViewById(R.id.tvFileName) // Nombre del archivo de grabación
        val playButton: Button = view.findViewById(R.id.btnPlay) // Botón para reproducir la grabación
    }

    // Método para crear nuevos ViewHolder cuando el RecyclerView lo necesite
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordingViewHolder {
        // Inflar el diseño de cada elemento desde el archivo XML (item_recording)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recording, parent, false)
        return RecordingViewHolder(view) // Devuelve un nuevo ViewHolder
    }

    // Método para vincular datos del archivo de grabación con los elementos de la interfaz
    override fun onBindViewHolder(holder: RecordingViewHolder, position: Int) {
        // Obtiene el archivo de grabación correspondiente a la posición actual
        val recording = recordings[position]

        // Establece el nombre del archivo en el TextView
        holder.fileName.text = recording.name

        // Configura el clic en el botón de reproducir para ejecutar la acción de callback
        holder.playButton.setOnClickListener { onItemClick(recording) }
    }

    // Método que devuelve el número total de elementos en la lista
    override fun getItemCount(): Int = recordings.size // Retorna la cantidad de grabaciones
}
