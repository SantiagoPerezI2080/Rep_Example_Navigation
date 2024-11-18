package com.santiago.navigationcomponentexample

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Button
import com.santiago.navigationcomponentexample.view.VoiceActivity
import com.santiago.navigationcomponentexample.view.CameraActivity
import com.santiago.navigationcomponentexample.view.UsuarioAdapter
import com.santiago.navigationcomponentexample.view.MainActivity




class ThirdFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_third, container, false)

        // Botón para abrir la grabadora
        val btnOpenRecorder = view.findViewById<Button>(R.id.btnOpenRecorder)
        btnOpenRecorder.setOnClickListener {
            val intent = Intent(activity, VoiceActivity::class.java)
            startActivity(intent)
        }

        // Botón para abrir la cámara
        val btnOpenCamera = view.findViewById<Button>(R.id.btnOpenCamera)
        btnOpenCamera.setOnClickListener {
            val intent = Intent(activity, CameraActivity::class.java)
            startActivity(intent)
        }

        // Botón para abrir Room (UsuarioAdapter o MainActivity relacionado con Room)
        val btnOpenRoom = view.findViewById<Button>(R.id.btnOpenRoom)
        btnOpenRoom.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }


        return view
    }
}
