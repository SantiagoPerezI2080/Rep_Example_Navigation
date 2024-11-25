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

    // Variables para vincular los elementos del layout y gestionar la lista de usuarios
    private lateinit var binding: ActivityRoomBinding
    private lateinit var usuarioAdapter: UsuarioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Activa la visualización en pantalla completa
        binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Deshabilita las opciones de selección y pulsación larga en los campos de texto
        binding.etNombre.apply {
            isLongClickable = false
            setTextIsSelectable(false)
        }

        binding.etApellido.apply {
            isLongClickable = false
            setTextIsSelectable(false)
        }

        // Obtiene la instancia de la base de datos y el DAO
        val db = UsuarioDatabaseProvider.getDatabase(applicationContext)
        val usuarioDao = db.getUserDao()

        // Inicializa el adaptador de usuarios para la lista
        usuarioAdapter = UsuarioAdapter(this, mutableListOf())
        binding.lvUsers.adapter = usuarioAdapter

        // Carga los usuarios existentes en la base de datos
        cargarUsuarios()

        // Configura el botón para agregar un usuario
        binding.btnAgregarUsuario.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val apellido = binding.etApellido.text.toString().trim()

            // Verifica que ambos campos no estén vacíos antes de agregar
            if (nombre.isNotEmpty() && apellido.isNotEmpty()) {
                agregarUsuario(nombre, apellido)
            } else {
                Toast.makeText(this, "Por favor, ingresa nombre y apellido.", Toast.LENGTH_SHORT).show()
            }
        }

        // Configura el botón para eliminar el usuario seleccionado
        binding.btnEliminarUsuario.setOnClickListener {
            val selectedUser = usuarioAdapter.getSelectedUser()
            if (selectedUser != null) {
                eliminarUsuario(selectedUser.id)
            } else {
                Toast.makeText(this, "Selecciona un usuario para eliminar.", Toast.LENGTH_SHORT).show()
            }
        }

        // Configura el botón para modificar el usuario seleccionado
        binding.btnModificarUsuario.setOnClickListener {
            val selectedUser = usuarioAdapter.getSelectedUser()
            if (selectedUser != null) {
                // Rellena los campos de texto con los datos del usuario seleccionado
                binding.etNombre.setText(selectedUser.nombre)
                binding.etApellido.setText(selectedUser.apellido)

                // Configura la acción de actualizar cuando se haga clic en "Agregar Usuario"
                binding.btnAgregarUsuario.setOnClickListener {
                    val nuevoNombre = binding.etNombre.text.toString().trim()
                    val nuevoApellido = binding.etApellido.text.toString().trim()

                    // Verifica que los campos no estén vacíos antes de actualizar
                    if (nuevoNombre.isNotEmpty() && nuevoApellido.isNotEmpty()) {
                        modificarUsuario(selectedUser.id, nuevoNombre, nuevoApellido)
                    } else {
                        Toast.makeText(this, "Por favor, ingresa nombre y apellido.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Selecciona un usuario para modificar.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Agrega un nuevo usuario a la base de datos
    private fun agregarUsuario(nombre: String, apellido: String) {
        val nuevoUsuario = UserEntity(nombre = nombre, apellido = apellido)

        lifecycleScope.launch(Dispatchers.IO) {
            val usuarioDao = UsuarioDatabaseProvider.getDatabase(applicationContext).getUserDao()
            usuarioDao.insertar(nuevoUsuario) // Inserta el usuario en la base de datos
            withContext(Dispatchers.Main) {
                // Limpia los campos de entrada y recarga la lista
                binding.etNombre.text.clear()
                binding.etApellido.text.clear()
                cargarUsuarios()
                Toast.makeText(applicationContext, "Usuario agregado.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Modifica un usuario existente en la base de datos
    private fun modificarUsuario(id: Int, nombre: String, apellido: String) {
        val usuarioModificado = UserEntity(id = id, nombre = nombre, apellido = apellido)

        lifecycleScope.launch(Dispatchers.IO) {
            val usuarioDao = UsuarioDatabaseProvider.getDatabase(applicationContext).getUserDao()
            usuarioDao.insertar(usuarioModificado) // Usa `insertar` ya que reemplaza registros con el mismo ID
            withContext(Dispatchers.Main) {
                // Limpia los campos de entrada y recarga la lista
                binding.etNombre.text.clear()
                binding.etApellido.text.clear()
                cargarUsuarios()
                Toast.makeText(applicationContext, "Usuario modificado.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Carga todos los usuarios desde la base de datos y actualiza el adaptador
    private fun cargarUsuarios() {
        val usuarioDao = UsuarioDatabaseProvider.getDatabase(applicationContext).getUserDao()
        lifecycleScope.launch(Dispatchers.IO) {
            val usuarios = usuarioDao.getAllUsers() // Obtiene todos los usuarios
            withContext(Dispatchers.Main) {
                usuarioAdapter.clear()
                usuarioAdapter.addAll(usuarios)
                usuarioAdapter.notifyDataSetChanged() // Notifica al adaptador de los cambios
            }
        }
    }

    // Elimina un usuario de la base de datos según su ID
    private fun eliminarUsuario(id: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val usuarioDao = UsuarioDatabaseProvider.getDatabase(applicationContext).getUserDao()
            usuarioDao.eliminarPorId(id) // Elimina el usuario con el ID especificado
            withContext(Dispatchers.Main) {
                cargarUsuarios() // Recarga la lista de usuarios
                Toast.makeText(applicationContext, "Usuario eliminado.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

