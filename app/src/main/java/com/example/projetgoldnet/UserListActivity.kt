package com.example.projetgoldnet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projetgoldnet.databinding.ActivityUserListBinding
import kotlinx.coroutines.launch

class UserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadUsersFromCloud()
        setupBackButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun loadUsersFromCloud() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getAllUsers()
                if (response.isSuccessful && response.body()?.success == true) {
                    val users = response.body()!!.users ?: emptyList()
                    binding.recyclerUsers.apply {
                        layoutManager = LinearLayoutManager(this@UserListActivity)
                        adapter = UserAdapter(users)
                    }
                } else {
                    toast("Error al cargar usuarios")
                }
            } catch (e: Exception) {
                toast("Sin conexión")
            }
        }
    }

    private fun setupBackButton() {
        binding.btnBackToDashboard.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}