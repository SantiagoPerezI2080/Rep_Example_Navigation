package com.santiago.navigationcomponentexample

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var tvStatus: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var rvRecordings: RecyclerView
    private var mediaRecorder: MediaRecorder? = null
    private var outputFilePath: String = ""
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.voice_recorder)  // Cambiar a voice_recorder.xml

        // Vincular elementos de la interfaz
        tvStatus = findViewById(R.id.tvStatus)
        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)
        rvRecordings = findViewById(R.id.rvRecordings)

        // Configuración de botones
        btnStart.setOnClickListener { startRecording() }
        btnStop.setOnClickListener { stopRecording() }

        // Verificar permisos de grabación
        checkPermissions()

        // Inicialización de RecyclerView y carga de grabaciones
        rvRecordings.layoutManager = LinearLayoutManager(this)
        loadRecordings()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 200)
            }
        }
    }

    private fun startRecording() {
        // Configurar el archivo de salida
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val outputDir = externalCacheDir?.absolutePath ?: filesDir.absolutePath
        outputFilePath = "$outputDir/recording_$timestamp.aac"


        // Configuración del MediaRecorder
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)  // Cambiar a AAC
            setOutputFile(outputFilePath)
            prepare()
            start()
        }

        // Actualizar estado y botones
        tvStatus.text = "Grabando..."
        btnStart.isEnabled = false
        btnStop.isEnabled = true
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null

        // Actualizar estado y botones
        tvStatus.text = "Grabación detenida. Archivo guardado en $outputFilePath"
        btnStart.isEnabled = true
        btnStop.isEnabled = false
        loadRecordings()  // Actualizar lista de grabaciones
    }

    private fun loadRecordings() {
        val recordingsDir = File(externalCacheDir?.absolutePath ?: filesDir.absolutePath)
        val recordings = recordingsDir.listFiles { file -> file.extension == "aac" }?.toList() ?: listOf()

        rvRecordings.adapter = RecordingAdapter(recordings) { file: File -> playRecording(file) }

    }

    private fun playRecording(file: File) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(file.absolutePath)
            prepare()
            start()
        }
        tvStatus.text = "Reproduciendo: ${file.name}"
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
