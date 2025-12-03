package com.example.projetgoldnet

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(private val users: List<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPhoto: CircleImageView = view.findViewById(R.id.ivUserPhoto)
        val tvName: TextView = view.findViewById(R.id.tvUserName)
        val tvEmail: TextView = view.findViewById(R.id.tvUserEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]

        holder.tvName.text = "${user.nombre} ${user.primerApellido} ${user.segundoApellido}"
        holder.tvEmail.text = user.correo

        if (user.fotoPerfil.isNotEmpty()) {
            try {
                val bytes = Base64.decode(user.fotoPerfil, android.util.Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                holder.ivPhoto.setImageBitmap(bitmap)
            } catch (e: Exception) {
                holder.ivPhoto.setImageResource(R.drawable.ic_profile_placeholder)
            }
        } else {
            holder.ivPhoto.setImageResource(R.drawable.ic_profile_placeholder)
        }
    }

    override fun getItemCount() = users.size
}