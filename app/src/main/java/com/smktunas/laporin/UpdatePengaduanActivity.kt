package com.smktunas.laporin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.smktunas.laporin.databinding.ActivityUpdatePengaduanBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UpdatePengaduanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdatePengaduanBinding
    private var pengaduanId: Int = -1
    private var selectedImageUri: Uri? = null
    private var kategoriList: List<Kategori> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePengaduanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil ID pengaduan dari intent
        pengaduanId = intent.getIntExtra("id_pengaduan", -1)
        if (pengaduanId == -1) {
            Toast.makeText(this, "ID pengaduan tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Load detail pengaduan
        loadDetail()

        // Load kategori untuk spinner
        loadKategori()

        // Pilih gambar
        binding.ivPreview.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        // Tombol cancel
        binding.btnCancel.setOnClickListener { finish() }

        // Tombol update
        binding.btnUpdate.setOnClickListener { updatePengaduan() }
    }

    private fun loadDetail() {
        val token = getSharedPreferences("user_session", MODE_PRIVATE)
            .getString("token", null) ?: return

        ApiClient.instance.getDetailPengaduan("Bearer $token", pengaduanId)
            .enqueue(object : Callback<Pengaduan> {
                override fun onResponse(call: Call<Pengaduan>, response: Response<Pengaduan>) {
                    if (response.isSuccessful) {
                        val data = response.body() ?: return
                        binding.etJudul.setText(data.judul)
                        binding.etIsi.setText(data.isi)

                        // Set preview gambar
                        if (!data.gambar.isNullOrEmpty()) {
                            val imageUrl = "http://192.168.1.6:8000/storage/${data.gambar}"
                            Glide.with(this@UpdatePengaduanActivity)
                                .load(imageUrl)
                                .into(binding.ivPreview)
                        } else {
                            binding.ivPreview.setImageResource(R.drawable.sample12)
                        }
                    } else {
                        Toast.makeText(this@UpdatePengaduanActivity, "Gagal memuat detail", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Pengaduan>, t: Throwable) {
                    Toast.makeText(this@UpdatePengaduanActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun loadKategori() {
        ApiClient.instance.getKategori()
            .enqueue(object : Callback<List<Kategori>> {
                override fun onResponse(call: Call<List<Kategori>>, response: Response<List<Kategori>>) {
                    if (response.isSuccessful) {
                        kategoriList = response.body() ?: emptyList()
                        val names = kategoriList.map { it.nama_kategori }
                        val adapter = ArrayAdapter(this@UpdatePengaduanActivity, android.R.layout.simple_spinner_item, names)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spKategori.adapter = adapter
                    }
                }

                override fun onFailure(call: Call<List<Kategori>>, t: Throwable) {
                    Toast.makeText(this@UpdatePengaduanActivity, "Gagal memuat kategori", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            binding.ivPreview.setImageURI(selectedImageUri)
        }
    }

    private fun updatePengaduan() {
        val token = getSharedPreferences("user_session", MODE_PRIVATE)
            .getString("token", null) ?: return

        val judulStr = binding.etJudul.text.toString().trim()
        val isiStr = binding.etIsi.text.toString().trim()
        val kategoriPos = binding.spKategori.selectedItemPosition
        if (kategoriPos == Spinner.INVALID_POSITION) {
            Toast.makeText(this, "Pilih kategori dulu", Toast.LENGTH_SHORT).show()
            return
        }
        val kategoriId = kategoriList[kategoriPos].id

        val judulPart = RequestBody.create("text/plain".toMediaTypeOrNull(), judulStr)
        val isiPart = RequestBody.create("text/plain".toMediaTypeOrNull(), isiStr)
        val kategoriPart = RequestBody.create("text/plain".toMediaTypeOrNull(), kategoriId.toString())

        val gambarPart = selectedImageUri?.let {
            val file = File(FileUtils.getPath(this, it))
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("gambar", file.name, requestFile)
        }

        ApiClient.instance.updatePengaduan(
            "Bearer $token",
            pengaduanId,
            judulPart,
            isiPart,
            kategoriPart,
            gambarPart
        ).enqueue(object : Callback<CreatePengaduanResponse> {
            override fun onResponse(call: Call<CreatePengaduanResponse>, response: Response<CreatePengaduanResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdatePengaduanActivity, "Update berhasil", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@UpdatePengaduanActivity, "Gagal update", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CreatePengaduanResponse>, t: Throwable) {
                Toast.makeText(this@UpdatePengaduanActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
