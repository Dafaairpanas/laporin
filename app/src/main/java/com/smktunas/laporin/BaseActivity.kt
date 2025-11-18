package com.smktunas.laporin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smktunas.laporin.ui.CreatePengaduanActivity

open class BaseActivity : AppCompatActivity() {

    fun setupBottomNav() {
        val include = findViewById<View>(R.id.include_bottom_nav) ?: return

        val navPengaduan = include.findViewById<LinearLayout>(R.id.nav_pengaduan)
        val navProfil = include.findViewById<LinearLayout>(R.id.nav_profil)
        val navAdd = include.findViewById<ImageButton>(R.id.nav_add)

        include.bringToFront()

        navPengaduan.setOnClickListener {
            startActivity(Intent(this, PengaduanActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        navAdd.setOnClickListener {
            startActivity(Intent(this, CreatePengaduanActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        navProfil.setOnClickListener {
            Toast.makeText(this, "Kamu sudah di halaman Profil", Toast.LENGTH_SHORT).show()
        }
    }
}
