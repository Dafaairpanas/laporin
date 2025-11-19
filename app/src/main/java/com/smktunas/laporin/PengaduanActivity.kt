package com.smktunas.laporin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smktunas.laporin.ui.CreatePengaduanActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PengaduanActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PengaduanAdapter
    private var pengaduanList: MutableList<Pengaduan> = mutableListOf()

    companion object {
        const val DETAIL_REQUEST_CODE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaduan)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        setupBottomNav()

        val token = getSharedPreferences("user_session", MODE_PRIVATE)
            .getString("token", null)

        if (token == null) {
            Toast.makeText(this, "Token tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        getMyPengaduan(token)
    }

    private fun getMyPengaduan(token: String) {
        val bearerToken = "Bearer $token"
        ApiClient.instance.getMyPengaduan(bearerToken)
            .enqueue(object : Callback<List<Pengaduan>> {
                override fun onResponse(call: Call<List<Pengaduan>>, response: Response<List<Pengaduan>>) {
                    if (response.isSuccessful) {
                        pengaduanList.clear()
                        pengaduanList.addAll(response.body() ?: emptyList())
                        adapter = PengaduanAdapter(pengaduanList)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this@PengaduanActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Pengaduan>>, t: Throwable) {
                    Toast.makeText(this@PengaduanActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setupBottomNav() {
        val navPengaduan = findViewById<LinearLayout>(R.id.nav_pengaduan)
        val navProfil = findViewById<LinearLayout>(R.id.nav_profil)
        val navAdd = findViewById<ImageButton>(R.id.nav_add)

        navPengaduan.setOnClickListener {
            Toast.makeText(this, "Kamu sudah di halaman Pengaduan", Toast.LENGTH_SHORT).show()
        }

        navAdd.setOnClickListener {
            startActivity(Intent(this, CreatePengaduanActivity::class.java))
        }

        navProfil.setOnClickListener {
            startActivity(Intent(this, ProfilActivity::class.java))
        }
    }

    // Menangani refresh list setelah update/delete dari DetailPengaduan
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DETAIL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val token = getSharedPreferences("user_session", MODE_PRIVATE)
                .getString("token", null)
            if (token != null) getMyPengaduan(token)
        }
    }
}
