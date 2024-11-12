package com.santiago.navigationcomponentexample

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Button

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

        return view
    }
}
