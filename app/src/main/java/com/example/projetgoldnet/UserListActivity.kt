package com.example.projetgoldnet

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projetgoldnet.databinding.ActivityUserListBinding


class UserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListBinding
    private val prefs by lazy { getSharedPreferences("users", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadUsers()
        setupBackButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // FLECHA ATRÁS
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun loadUsers() {
        val users = prefs.all.mapNotNull { (id, dataStr) ->
            val data = dataStr.toString().split("|")
            if (data.size >= 12) {
                val bitmap = if (data[11].isNotEmpty()) {
                    val bytes = android.util.Base64.decode(data[11], android.util.Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                } else null
                User(id, "${data[0]} ${data[1]} ${data[2]}", data[4], bitmap)
            } else null
        }

        binding.recyclerUsers.apply {
            layoutManager = LinearLayoutManager(this@UserListActivity)
            adapter = UserAdapter(users)
        }
    }

    private fun setupBackButton() {
        binding.btnBackToDashboard.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        return true
    }
}