package com.smktunas.laporin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LaporanActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PengaduanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan)

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
                            this@LaporanActivity,
                            "Gagal memuat data: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Pengaduan>>, t: Throwable) {
                    Log.e("LaporanActivity", "API error: ${t.message}", t)
                    Toast.makeText(this@LaporanActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

}
