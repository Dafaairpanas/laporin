package com.smktunas.laporin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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

        ivProfile = findViewById(R.id.ivProfile)
        tvUserTitle = findViewById(R.id.tvUserTitle)
        tvEmail = findViewById(R.id.tvEmail)
        btnLogout = findViewById(R.id.btnLogout)

        // Ambil data user dari SharedPreferences
        val pref = getSharedPreferences("user_session", MODE_PRIVATE)
        val name = pref.getString("name", "User")
        val email = pref.getString("email", "user@example.com")

        tvUserTitle.text = name
        tvEmail.text = email
        ivProfile.setImageResource(R.drawable.iconoir__profile_circle) // bisa diganti ambil foto user jika ada

        setupLogout()
        setupBottomNav()
    }

    private fun setupLogout() {
        btnLogout.setOnClickListener {
            // Hapus session
            val pref = getSharedPreferences("user_session", MODE_PRIVATE)
            pref.edit().clear().apply()

            Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show()

            // Kembali ke LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setupBottomNav() {
        val include = findViewById<View>(R.id.include_bottom_nav)
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
