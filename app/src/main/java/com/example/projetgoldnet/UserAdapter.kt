package com.example.projetgoldnet

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
        holder.tvName.text = user.fullName
        holder.tvEmail.text = user.email
        user.photoBitmap?.let { holder.ivPhoto.setImageBitmap(it) }
            ?: holder.ivPhoto.setImageResource(R.drawable.ic_profile_placeholder)
    }

    override fun getItemCount() = users.size
}