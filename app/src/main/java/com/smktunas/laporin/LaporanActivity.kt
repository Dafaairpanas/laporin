package com.smktunas.laporin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smktunas.laporin.ui.BuatPengaduanActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LaporanActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fun setupBottomNav() {
            // 🔹 Ambil layout yang di-include

            val navPengaduan = findViewById<LinearLayout>(R.id.nav_pengaduan)
            val navProfil = findViewById<LinearLayout>(R.id.nav_profil)
            val navAdd = findViewById<ImageButton>(R.id.nav_add)


            // 🔸 Tombol Pengaduan
            navPengaduan.setOnClickListener {
                Toast.makeText(this, "Kamu sudah di halaman Pengaduan", Toast.LENGTH_SHORT).show()

//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }

            // 🔸 Tombol Tambah
            navAdd.setOnClickListener {
                startActivity(Intent(this, BuatPengaduanActivity::class.java))
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }

            // 🔸 Tombol Profil (aktif sekarang)
            navProfil.setOnClickListener {
                startActivity(Intent(this, ProfilActivity::class.java))

            }

            // Cek login
            val pref = getSharedPreferences("user_session", MODE_PRIVATE)
            val token = pref.getString("token", null)
            if (token == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return
            }

            enableEdgeToEdge()
            setContentView(R.layout.activity_laporan)

            recyclerView = findViewById(R.id.recyclerView)
            emptyStateLayout = findViewById(R.id.emptyStateLayout)

            recyclerView.layoutManager = LinearLayoutManager(this)

            // Ambil data laporan
            ApiClient.instance.getPengaduan().enqueue(object : Callback<List<Pengaduan>> {
                override fun onResponse(
                    call: Call<List<Pengaduan>>,
                    response: Response<List<Pengaduan>>
                ) {
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
                        Toast.makeText(
                            this@LaporanActivity,
                            "Gagal memuat data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Pengaduan>>, t: Throwable) {
                    Toast.makeText(
                        this@LaporanActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}
