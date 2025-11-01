package com.example.goldnet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // seed sample data once (optional)
        SampleData.seed()

        // Simple delay or direct start
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    class SampleData {
        companion object {
            fun seed() {
                TODO("Not yet implemented")
            }
        }

    }
}

