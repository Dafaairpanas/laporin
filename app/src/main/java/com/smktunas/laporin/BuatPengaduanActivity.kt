package com.smktunas.laporin.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.smktunas.laporin.R
import com.smktunas.laporin.CustomBackButton
import com.smktunas.laporin.PengaduanActivity

class BuatPengaduanActivity : AppCompatActivity() {

    private lateinit var etJudul: TextInputEditText
    private lateinit var etKeluhan: TextInputEditText
    private lateinit var autoKategori: AutoCompleteTextView
    private lateinit var layoutUpload: LinearLayout
    private lateinit var btnKirim: Button
    private lateinit var imageView: ImageView
    private lateinit var customBackButton: CustomBackButton

    private var selectedImageUri: Uri? = null

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                selectedImageUri = result.data!!.data
                imageView.setImageURI(selectedImageUri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah) // ganti sesuai nama layout kamu

        // Inisialisasi view
        etJudul = findViewById(R.id.etJudul)
        etKeluhan = findViewById(R.id.etKeluhan)
        autoKategori = findViewById(R.id.autoKategori)
        layoutUpload = findViewById(R.id.layoutUpload)
        btnKirim = findViewById(R.id.btnKirim)
        imageView = findViewById(R.id.ic_image)
        customBackButton = findViewById(R.id.customBackButton)

        setupDropdown()
        setupUpload()
        setupKirim()
        setupBack()
    }

    private fun setupDropdown() {
        val kategoriList = listOf(
            "Kebersihan",
            "Keamanan",
            "Fasilitas Umum",
            "Pelayanan Publik",
            "Sosial",
            "Lainnya"
        )

        // Gunakan ArrayAdapter bawaan Android
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            kategoriList
        )
        autoKategori.setAdapter(adapter)
    }

    private fun setupUpload() {
        layoutUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            pickImage.launch(Intent.createChooser(intent, "Pilih Gambar"))
        }
    }

    private fun setupKirim() {
        btnKirim.setOnClickListener {
            val judul = etJudul.text?.toString()?.trim() ?: ""
            val keluhan = etKeluhan.text?.toString()?.trim() ?: ""
            val kategori = autoKategori.text?.toString()?.trim() ?: ""

            if (judul.isEmpty() || keluhan.isEmpty() || kategori.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(
                this,
                "Judul: $judul\nKategori: $kategori\nKeluhan: $keluhan",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setupBack() {
        customBackButton.setOnClickListener {
            finish()
        }
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
            startActivity(Intent(this, PengaduanActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        // 🔸 Tombol Tambah
        navAdd.setOnClickListener {
            startActivity(Intent(this, BuatPengaduanActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        // 🔸 Tombol Profil (aktif sekarang)
        navProfil.setOnClickListener {
            Toast.makeText(this, "Kamu sudah di halaman Profil", Toast.LENGTH_SHORT).show()
        }
    }
}
