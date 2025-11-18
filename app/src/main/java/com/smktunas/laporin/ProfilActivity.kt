package com.smktunas.laporin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.smktunas.laporin.ui.CreatePengaduanActivity

class ProfilActivity : AppCompatActivity() {

    private lateinit var ivProfile: ImageView
    private lateinit var tvUserTitle: TextView
    private lateinit var tvEmail: TextView
    private lateinit var btnLogout: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        // --- Inisialisasi view profil ---
        ivProfile = findViewById(R.id.ivProfile)
        tvUserTitle = findViewById(R.id.tvUserTitle)
        tvEmail = findViewById(R.id.tvEmail)
        btnLogout = findViewById(R.id.btnLogout)

        // --- Data dummy ---
        tvUserTitle.text = "Dafa Putra"
        tvEmail.text = "dafa.putra@example.com"
        ivProfile.setImageResource(R.drawable.sample12)

        setupLogout()
        setupBottomNav()
    }

    private fun setupLogout() {
        btnLogout.setOnClickListener {
            Toast.makeText(this, "Logout berhasil (dummy)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNav() {
        // 🔹 Ambil layout yang di-include
        val include = findViewById<View>(R.id.include_bottom_nav)

        val navPengaduan = include.findViewById<LinearLayout>(R.id.nav_pengaduan)
        val navProfil = include.findViewById<LinearLayout>(R.id.nav_profil)
        val navAdd = include.findViewById<ImageButton>(R.id.nav_add)

        include.bringToFront()

        // 🔸 Tombol Pengaduan
        navPengaduan.setOnClickListener {
            startActivity(Intent(this, PengaduanActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        // 🔸 Tombol Tambah
        navAdd.setOnClickListener {
            startActivity(Intent(this, CreatePengaduanActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        // 🔸 Tombol Profil (aktif sekarang)
        navProfil.setOnClickListener {
            Toast.makeText(this, "Kamu sudah di halaman Profil", Toast.LENGTH_SHORT).show()
        }
    }
}
