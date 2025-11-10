package com.smktunas.laporin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class LaporanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ✅ panggil layout dulu agar tidak blank
        setContentView(R.layout.activity_laporan)
        enableEdgeToEdge()

        // ✅ periksa token setelah layout tampil
        val pref = getSharedPreferences("user_session", MODE_PRIVATE)
        val token = pref.getString("token", null)
        if (token == null) {
            // Jika belum login, arahkan ke LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // ✅ di sini nanti isi fitur laporanmu
    }
}
