package com.smktunas.laporin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smktunas.laporin.ui.BuatPengaduanActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PengaduanActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan)

        recyclerView = findViewById(R.id.recyclerView)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)

        recyclerView.layoutManager = LinearLayoutManager(this)

        ApiClient.instance.getPengaduan().enqueue(object : Callback<List<Pengaduan>> {
            override fun onResponse(call: Call<List<Pengaduan>>, response: Response<List<Pengaduan>>) {
                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()

                    if (data.isEmpty()) {
                        // Tampilkan empty state
                        emptyStateLayout.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        // Tampilkan data
                        emptyStateLayout.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        recyclerView.adapter = PengaduanAdapter(data)
                    }
                } else {
                    Toast.makeText(this@PengaduanActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Pengaduan>>, t: Throwable) {
                Toast.makeText(this@PengaduanActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
        setupBottomNav()
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
            Toast.makeText(this, "Kamu sudah di halaman Laporan", Toast.LENGTH_SHORT).show()
        }

        // 🔸 Tombol Tambah
        navAdd.setOnClickListener {
            startActivity(Intent(this, BuatPengaduanActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        // 🔸 Tombol Profil (aktif sekarang)
        navProfil.setOnClickListener {
            startActivity(Intent(this, ProfilActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
