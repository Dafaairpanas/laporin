package com.smktunas.laporin

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DaftarActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar)

        val login = findViewById<TextView>(R.id.tvLinkRegister)
        login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}