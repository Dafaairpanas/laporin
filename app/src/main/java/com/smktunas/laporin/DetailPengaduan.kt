package com.smktunas.laporin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.smktunas.laporin.databinding.ActivityDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPengaduan : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var pengaduanId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil ID pengaduan
        pengaduanId = intent.getIntExtra("id_pengaduan", -1)
        if (pengaduanId == -1) {
            Toast.makeText(this, "ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadDetail()

        // Tombol back
        binding.customBackButton.setOnClickListener { finish() }
    }

    private fun loadDetail() {
        val token = getSharedPreferences("user_session", MODE_PRIVATE)
            .getString("token", null)

        if (token == null) {
            Toast.makeText(this, "Token hilang, silakan login lagi", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        ApiClient.instance.getDetailPengaduan("Bearer $token", pengaduanId)
            .enqueue(object : Callback<Pengaduan> {
                override fun onResponse(call: Call<Pengaduan>, response: Response<Pengaduan>) {
                    if (response.isSuccessful) {
                        val data = response.body() ?: return
                        binding.tvTitle.text = data.judul ?: "-"
                        binding.tvKategori.text = data.kategori?.nama_kategori ?: "-"
                        binding.tvKeluhan.text = data.isi ?: "-"

                        // Perbaiki nama pengirim
                        binding.tvPengirim.text = if (data.is_anonymous == 1) {
                            "Anonim"
                        } else {
                            data.user?.name ?: "Tidak diketahui"
                        }

                        // Tampilkan gambar jika ada
                        if (!data.gambar.isNullOrEmpty()) {
                            val imageUrl = "http://192.168.3.13:8000/storage/${data.gambar}"
                            Glide.with(this@DetailPengaduan)
                                .load(imageUrl)
                                .placeholder(R.drawable.sample12)
                                .into(binding.ivKeluhan)
                        } else {
                            binding.ivKeluhan.setImageResource(R.drawable.sample12)
                        }

                    } else {
                        Toast.makeText(this@DetailPengaduan, "Gagal memuat detail", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Pengaduan>, t: Throwable) {
                    Toast.makeText(this@DetailPengaduan, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
