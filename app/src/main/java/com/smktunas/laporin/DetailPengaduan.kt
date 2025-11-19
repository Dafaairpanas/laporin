package com.smktunas.laporin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.smktunas.laporin.databinding.ActivityDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPengaduan : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var pengaduanId: Int = -1

    companion object {
        const val UPDATE_REQUEST_CODE = 100
    }

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

        // Load detail
        loadDetail()

        // Tombol back
        binding.customBackButton.setOnClickListener { finish() }

        // Tombol edit
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, UpdatePengaduanActivity::class.java)
            intent.putExtra("id_pengaduan", pengaduanId)
            startActivityForResult(intent, UPDATE_REQUEST_CODE)
        }

        // Tombol delete
        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Hapus Pengaduan")
                .setMessage("Yakin ingin menghapus pengaduan ini?")
                .setPositiveButton("Hapus") { _, _ -> hapusPengaduan() }
                .setNegativeButton("Batal", null)
                .show()
        }
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
                        binding.tvPengirim.text = if (data.is_anonymous == 1) "Anonim" else "User #${data.user_id ?: "-"}"
                        binding.tvKeluhan.text = data.isi ?: "-"

                        if (!data.gambar.isNullOrEmpty()) {
                            val imageUrl = "http://192.168.1.6:8000/storage/${data.gambar}"
                            Glide.with(this@DetailPengaduan)
                                .load(imageUrl)
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

    private fun hapusPengaduan() {
        val token = getSharedPreferences("user_session", MODE_PRIVATE)
            .getString("token", null)

        if (token == null) {
            Toast.makeText(this, "Token hilang, silakan login lagi", Toast.LENGTH_SHORT).show()
            return
        }

        ApiClient.instance.deletePengaduan("Bearer $token", pengaduanId)
            .enqueue(object : Callback<DeleteResponse> {
                override fun onResponse(call: Call<DeleteResponse>, response: Response<DeleteResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@DetailPengaduan, "Pengaduan berhasil dihapus", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK) // beri tahu PengaduanActivity untuk refresh
                        finish()
                    } else {
                        Toast.makeText(this@DetailPengaduan, "Gagal menghapus pengaduan", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                    Toast.makeText(this@DetailPengaduan, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            loadDetail() // auto refresh setelah update
            setResult(Activity.RESULT_OK) // beri tahu PengaduanActivity untuk refresh list
        }
    }
}
