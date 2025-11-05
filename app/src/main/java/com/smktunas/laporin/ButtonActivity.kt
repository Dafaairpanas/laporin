package com.smktunas.laporin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.smktunas.laporin.ui.BuatPengaduanActivity

class ButtonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_button)

        // Inisialisasi tombol navigasi
        val navPengaduan = findViewById<LinearLayout>(R.id.nav_pengaduan)
        val navAdd = findViewById<ImageButton>(R.id.nav_add)
        val navProfil = findViewById<LinearLayout>(R.id.nav_profil)

        // --- Tombol Pengaduan ---
        navPengaduan.setOnClickListener {
            val intent = Intent(this, PengaduanActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish() // agar tidak menumpuk activity
        }

        // --- Tombol Tambah Pengaduan ---
        navAdd.setOnClickListener {
            val intent = Intent(this, BuatPengaduanActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        // --- Tombol Profil ---
        navProfil.setOnClickListener {
            val intent = Intent(this, ProfilActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}
