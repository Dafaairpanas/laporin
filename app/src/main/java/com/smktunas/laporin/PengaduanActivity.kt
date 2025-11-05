package com.smktunas.laporin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PengaduanActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        ApiClient.instance.getPengaduan().enqueue(object : Callback<List<Pengaduan>> {
            override fun onResponse(call: Call<List<Pengaduan>>, response: Response<List<Pengaduan>>) {
                if (response.isSuccessful) {
                    recyclerView.adapter = PengaduanAdapter(response.body() ?: emptyList())
                } else {
                    Toast.makeText(this@PengaduanActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Pengaduan>>, t: Throwable) {
                Toast.makeText(this@PengaduanActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
