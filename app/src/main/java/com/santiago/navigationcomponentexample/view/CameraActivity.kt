package com.santiago.navigationcomponentexample.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.cristhian.miprimeraapp.Constants
import com.santiago.navigationcomponentexample.R
import com.santiago.navigationcomponentexample.databinding.ActivityCameraBinding
import java.io.File

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding

    // Variables para gestionar la cámara y la captura de imágenes
    private var preview: Preview? = null // Muestra lo que ve la cámara en tiempo real
    private var imageCapture: ImageCapture? = null // Gestiona la captura de imágenes

    // Variables para guardar las fotos capturadas
    private lateinit var outputDirectory: File // Directorio para guardar las fotos
    private var photoFile: File? = null // Archivo de la foto capturada
    private var savedUri: Uri? = null // URI de la última foto guardada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Configuración visual para pantallas completas
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar directorio de almacenamiento
        outputDirectory = getOutputDirectory()

        // Verificar permisos necesarios
        checkPermissions()

        if (allPermissionsGranted()) {
            startCamera() // Inicia la cámara si los permisos están otorgados
        } else {
            ActivityCompat.requestPermissions(
                this, Constants.REQUIRED_PERMISSIONS, Constants.REQUEST_CODE_PERMISSIONS
            )
        }

        // Configurar el botón para capturar fotos
        binding.ivBtnCamera.setOnClickListener {
            takePhoto() // Captura la foto
        }

        // Configurar el botón para abrir la última imagen capturada
        binding.ivBtnSave.setOnClickListener {
            savedUri?.let { uri ->
                val intent = Intent(this, ImagenCapturadaActivity::class.java).apply {
                    putExtra("uri", uri.toString())
                }
                startActivity(intent) // Abre la actividad para mostrar la imagen
            } ?: showToastDialog(this, "No se ha capturado ninguna imagen.")
        }

        // Oculta el botón de abrir imagen hasta que se capture una
        binding.ivBtnSave.isVisible = false
    }

    // Verifica y solicita permisos para usar la cámara y el almacenamiento
    private fun checkPermissions() {
        if (
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), 0
            )
            startCamera() // Intenta iniciar la cámara tras solicitar permisos
        }
    }

    // Comprueba si todos los permisos están otorgados
    private fun allPermissionsGranted() = arrayOf(Manifest.permission.CAMERA).all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    // Inicia la cámara y configura los componentes necesarios
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Configura el componente de vista previa
            preview = Preview.Builder()
                .setTargetResolution(Size(1280, 720)) // Tamaño de la resolución
                .build()

            // Selecciona la cámara trasera
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            try {
                // Desvincula otros casos de uso y configura los nuevos
                cameraProvider.unbindAll()

                // Configura el componente de captura de imágenes
                imageCapture = ImageCapture.Builder()
                    .setTargetResolution(Size(1280, 720))
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

                // Vincula los casos de uso al ciclo de vida de la actividad
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )

                // Configura la superficie para la vista previa
                preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            } catch (e: Exception) {
                Log.e(Constants.TAG, "Caso de uso falla", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    // Captura una foto y la guarda en el directorio
    private fun takePhoto() {
        val randomNumber = (100..999).shuffled().last()
        val imageCapture = imageCapture ?: return // Verifica que `imageCapture` no sea nulo

        // Define el archivo de salida
        photoFile = File(outputDirectory, "camara_personalizada_$randomNumber.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile!!).build()

        // Captura la foto y maneja los resultados
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    Log.d(Constants.TAG, "Fallo al guardar", exception)
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    savedUri = Uri.fromFile(photoFile)
                    showToastDialog(binding.root.context, "Se ha guardado en $savedUri")
                    Log.d(Constants.TAG, "Guardado en la uri -> $savedUri")

                    // Muestra el botón para abrir la imagen capturada
                    binding.ivBtnSave.isVisible = true
                }
            }
        )
    }

    // Obtiene el directorio para almacenar imágenes
    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let { mFile ->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    // Muestra un mensaje en pantalla
    private fun showToastDialog(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}

