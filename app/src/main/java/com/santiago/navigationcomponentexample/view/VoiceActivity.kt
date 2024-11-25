package com.santiago.navigationcomponentexample.view

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.santiago.navigationcomponentexample.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.io.File

class VoiceActivity : AppCompatActivity() {
    // Elementos de la interfaz
    private lateinit var tvStatus: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var rvRecordings: RecyclerView

    // Variables para la grabación y reproducción
    private var mediaRecorder: MediaRecorder? = null
    private var outputFilePath: String = ""
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice)

        // Vinculación de elementos de la interfaz con variables
        tvStatus = findViewById(R.id.tvStatus)
        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)
        rvRecordings = findViewById(R.id.rvRecordings)

        // Configuración de botones para grabar y detener
        btnStart.setOnClickListener { startRecording() }
        btnStop.setOnClickListener { stopRecording() }

        // Verifica los permisos para grabar audio
        checkPermissions()

        // Configura el RecyclerView para mostrar grabaciones
        rvRecordings.layoutManager = LinearLayoutManager(this)
        loadRecordings() // Carga las grabaciones existentes
    }

    private fun checkPermissions() {
        // Verifica si el permiso para grabar audio está otorgado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 200)
            }
        }
    }

    private fun startRecording() {
        // Configura el archivo de salida para la grabación
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val outputDir = externalCacheDir?.absolutePath ?: filesDir.absolutePath
        outputFilePath = "$outputDir/recording_$timestamp.aac"

        // Configura MediaRecorder para iniciar la grabación
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFilePath)
            prepare() // Prepara el recorder
            start()   // Inicia la grabación
        }

        // Actualiza el estado y los botones de la interfaz
        tvStatus.text = "Grabando..."
        btnStart.isEnabled = false
        btnStop.isEnabled = true
    }

    private fun stopRecording() {
        // Detiene y libera el MediaRecorder
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null

        // Actualiza el estado de la interfaz
        tvStatus.text = "Grabación detenida. Archivo guardado en $outputFilePath"
        btnStart.isEnabled = true
        btnStop.isEnabled = false
        loadRecordings() // Recarga la lista de grabaciones
    }

    private fun loadRecordings() {
        // Obtiene todas las grabaciones en el directorio de almacenamiento
        val recordingsDir = File(externalCacheDir?.absolutePath ?: filesDir.absolutePath)
        val recordings = recordingsDir.listFiles { file -> file.extension == "aac" }?.toList() ?: listOf()

        // Configura el adaptador para el RecyclerView
        rvRecordings.adapter = RecordingAdapter(recordings) { file: File -> playRecording(file) }
    }

    private fun playRecording(file: File) {
        // Configura y reproduce la grabación seleccionada
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(file.absolutePath)
            prepare()
            start()
        }
        tvStatus.text = "Reproduciendo: ${file.name}"
    }

    override fun onDestroy() {
        // Libera el MediaPlayer al destruir la actividad
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

