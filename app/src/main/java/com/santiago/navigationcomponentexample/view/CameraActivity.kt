package com.santiago.navigationcomponentexample.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.ContactsContract.Directory
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cristhian.miprimeraapp.Constants
import com.santiago.navigationcomponentexample.R
import com.santiago.navigationcomponentexample.databinding.ActivityCameraBinding
import java.io.File

class CameraActivity : AppCompatActivity() {

    lateinit var binding: ActivityCameraBinding

    // Visualizar lo que el sensor esta capturando
    private var preview:Preview? = null
    private var imageCapture:ImageCapture? = null
    private var imageAnalysis:ImageAnalysis? = null

    private var camera:Camera? = null

    // Guardar foto en el storage
    private lateinit var outputDirectory: File
    var photoFile: File? = null
    var savedUri:Uri? = null

    // Variable para el temporizador
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermissions()

        if(allPermissionsGranted()){
            startCamera()
        }else{
            ActivityCompat.requestPermissions(
                this, Constants.REQUIRED_PERMISSIONS, Constants.REQUEST_CODE_PERMISSIONS
            )
        }
    }

    // Permisos Manifest
    private fun checkPermissions() {
        if (
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO
            )
            != PackageManager.PERMISSION_GRANTED
            ||
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
            ||
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), 0
            )
            startCamera()
        }
    }
}