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

    private var selectedUser: UserEntity? = null

    fun getSelectedUser(): UserEntity? = selectedUser

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false)
        val usuario = getItem(position) ?: return view

        val tvNombre = view.findViewById<TextView>(R.id.textViewNombre)
        val tvApellido = view.findViewById<TextView>(R.id.textViewApellido)

        tvNombre.text = usuario.nombre
        tvApellido.text = usuario.apellido

        // Resaltar si el usuario está seleccionado
        if (usuario == selectedUser) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.selected_item))
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        }


        // Manejar selección al hacer clic
        view.setOnClickListener {
            selectedUser = if (selectedUser == usuario) null else usuario
            notifyDataSetChanged()
        }

        return view
    }
}
