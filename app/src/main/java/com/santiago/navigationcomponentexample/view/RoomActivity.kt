package com.santiago.navigationcomponentexample.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.santiago.navigationcomponentexample.databinding.ActivityRoomBinding
import com.santiago.navigationcomponentexample.model.database.entities.UserEntity
import com.santiago.navigationcomponentexample.model.database.providers.UsuarioDatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoomBinding
    private lateinit var usuarioAdapter: UsuarioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etNombre.apply {
            isLongClickable = false
            setTextIsSelectable(false)
        }

        binding.etApellido.apply {
            isLongClickable = false
            setTextIsSelectable(false)
        }


        val db = UsuarioDatabaseProvider.getDatabase(applicationContext)
        val usuarioDao = db.getUserDao()

        // Inicializar el adaptador
        usuarioAdapter = UsuarioAdapter(this, mutableListOf())
        binding.lvUsers.adapter = usuarioAdapter

        // Cargar usuarios
        cargarUsuarios()

        // Agregar usuario
        binding.btnAgregarUsuario.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val apellido = binding.etApellido.text.toString().trim()

            if (nombre.isNotEmpty() && apellido.isNotEmpty()) {
                agregarUsuario(nombre, apellido)
            } else {
                Toast.makeText(this, "Por favor, ingresa nombre y apellido.", Toast.LENGTH_SHORT).show()
            }
        }

        // Eliminar usuario seleccionado
        binding.btnEliminarUsuario.setOnClickListener {
            val selectedUser = usuarioAdapter.getSelectedUser()
            if (selectedUser != null) {
                eliminarUsuario(selectedUser.id)
            } else {
                Toast.makeText(this, "Selecciona un usuario para eliminar.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun agregarUsuario(nombre: String, apellido: String) {
        val nuevoUsuario = UserEntity(nombre = nombre, apellido = apellido)

        lifecycleScope.launch(Dispatchers.IO) {
            val usuarioDao = UsuarioDatabaseProvider.getDatabase(applicationContext).getUserDao()
            usuarioDao.insertar(nuevoUsuario)
            withContext(Dispatchers.Main) {
                binding.etNombre.text.clear()
                binding.etApellido.text.clear()
                cargarUsuarios()
                Toast.makeText(applicationContext, "Usuario agregado.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarUsuarios() {
        val usuarioDao = UsuarioDatabaseProvider.getDatabase(applicationContext).getUserDao()
        lifecycleScope.launch(Dispatchers.IO) {
            val usuarios = usuarioDao.getAllUsers()
            withContext(Dispatchers.Main) {
                usuarioAdapter.clear()
                usuarioAdapter.addAll(usuarios)
                usuarioAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun eliminarUsuario(id: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val usuarioDao = UsuarioDatabaseProvider.getDatabase(applicationContext).getUserDao()
            usuarioDao.eliminarPorId(id)
            withContext(Dispatchers.Main) {
                cargarUsuarios()
                Toast.makeText(applicationContext, "Usuario eliminado.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
