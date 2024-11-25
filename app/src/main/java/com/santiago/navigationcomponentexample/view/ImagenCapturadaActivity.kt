package com.santiago.navigationcomponentexample.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.santiago.navigationcomponentexample.databinding.ActivityImagenCapturadaBinding

class ImagenCapturadaActivity : AppCompatActivity() {

    // Variable de enlace para acceder a los elementos del diseño
    lateinit var binding: ActivityImagenCapturadaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Configuración visual para pantallas completas
        binding = ActivityImagenCapturadaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtiene la URI de la imagen capturada desde los extras del intent
        val bundle = intent.extras
        val uriImage = bundle?.getString("uri")

        // Establece la URI en el ImageView para mostrar la imagen capturada
        binding.ivImagenCapturada.setImageURI(uriImage?.toUri())
    }
}
