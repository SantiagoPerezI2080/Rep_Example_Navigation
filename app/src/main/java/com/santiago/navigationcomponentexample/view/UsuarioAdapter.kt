package com.santiago.navigationcomponentexample.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.santiago.navigationcomponentexample.R
import com.santiago.navigationcomponentexample.model.database.entities.UserEntity

class UsuarioAdapter(
    context: Context,
    usuarios: MutableList<UserEntity>
) : ArrayAdapter<UserEntity>(context, R.layout.item_usuario, usuarios) {

    // Variable para almacenar el usuario seleccionado
    private var selectedUser: UserEntity? = null

    // Método para obtener el usuario actualmente seleccionado
    fun getSelectedUser(): UserEntity? = selectedUser

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Infla el layout `item_usuario` si `convertView` es nulo
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false)

        // Obtiene el usuario actual en la posición dada
        val usuario = getItem(position) ?: return view

        // Referencias a los TextViews en el layout `item_usuario`
        val tvNombre = view.findViewById<TextView>(R.id.textViewNombre)
        val tvApellido = view.findViewById<TextView>(R.id.textViewApellido)

        // Asigna los datos del usuario a los TextViews
        tvNombre.text = usuario.nombre
        tvApellido.text = usuario.apellido

        // Cambia el color de fondo del elemento si está seleccionado
        if (usuario == selectedUser) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.selected_item))
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        }

        // Maneja la selección al hacer clic en un elemento
        view.setOnClickListener {
            // Si el usuario actual ya está seleccionado, lo deselecciona; de lo contrario, lo selecciona
            selectedUser = if (selectedUser == usuario) null else usuario
            notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado para actualizar la vista
        }

        return view
    }
}

