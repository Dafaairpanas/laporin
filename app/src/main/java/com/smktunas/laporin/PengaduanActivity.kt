package com.smktunas.laporin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.smktunas.laporin.ui.BuatPengaduanActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PengaduanActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PengaduanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaduan)
        setupBottomNav()

        // Ambil token
        val pref = getSharedPreferences("user_session", MODE_PRIVATE)
        val token = pref.getString("token", null)

        if (token == null) {
            Toast.makeText(this, "Token tidak ditemukan, silakan login ulang", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Panggil API pengaduan saya
        getMyPengaduan(token)
    }

    private fun getMyPengaduan(token: String) {
        val bearerToken = "Bearer $token"
        Log.d("LaporanActivity", "Token dikirim: $bearerToken")

        ApiClient.instance.getMyPengaduan(bearerToken)
            .enqueue(object : Callback<List<Pengaduan>> {
                override fun onResponse(
                    call: Call<List<Pengaduan>>,
                    response: Response<List<Pengaduan>>
                ) {
                    if (response.isSuccessful) {
                        val pengaduanList = response.body() ?: emptyList()
                        adapter = PengaduanAdapter(pengaduanList)
                        recyclerView.adapter = adapter
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("LaporanActivity", "Error body: $errorBody")
                        Toast.makeText(
                            this@PengaduanActivity,
                            "Gagal memuat data: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Pengaduan>>, t: Throwable) {
                    Log.e("LaporanActivity", "API error: ${t.message}", t)
                    Toast.makeText(this@PengaduanActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
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
