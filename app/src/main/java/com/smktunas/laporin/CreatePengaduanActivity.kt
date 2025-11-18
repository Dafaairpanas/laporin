package com.smktunas.laporin.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.smktunas.laporin.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.FileInputStream
import kotlin.collections.forEach

class CreatePengaduanActivity : AppCompatActivity() {

    private lateinit var etJudul: TextInputEditText
    private lateinit var etKeluhan: TextInputEditText
    private lateinit var autoKategori: AutoCompleteTextView
    private lateinit var layoutUpload: LinearLayout
    private lateinit var btnKirim: Button
    private lateinit var imagePreview: ImageView
    private lateinit var customBackButton: CustomBackButton

    private var selectedImageUri: Uri? = null
    private var selectedKategoriId: Int = 0

    // nama -> id
    private val kategoriMap = mutableMapOf<String, Int>()

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                selectedImageUri = result.data!!.data
                imagePreview.setImageURI(selectedImageUri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pengaduan)

        initViews()
        fetchKategori()
        setupUpload()
        setupKirim()
        setupBack()
    }

    private fun initViews() {
        etJudul = findViewById(R.id.etJudul)
        etKeluhan = findViewById(R.id.etKeluhan)
        autoKategori = findViewById(R.id.autoKategori)
        layoutUpload = findViewById(R.id.layoutUpload)
        btnKirim = findViewById(R.id.btnKirim)
        imagePreview = findViewById(R.id.ic_image)
        customBackButton = findViewById(R.id.customBackButton)
    }

    // Ambil kategori dari API dan isi dropdown
    private fun fetchKategori() {
        ApiClient.instance.getKategori().enqueue(object : Callback<List<Kategori>> {
            override fun onResponse(call: Call<List<Kategori>>, response: Response<List<Kategori>>) {
                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()!!

                    // Ambil nama kategori (String)
                    val namaKategori: List<String> = list.map { it.nama_kategori }

                    // Isi map nama -> id (aman)
                    kategoriMap.clear()
                    list.forEach { kategori ->
                        kategoriMap[kategori.nama_kategori] = kategori.id
                    }

                    // Adapter eksplisit String
                    val adapter = ArrayAdapter<String>(
                        this@CreatePengaduanActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        namaKategori
                    )
                    autoKategori.setAdapter(adapter)
                } else {
                    Toast.makeText(this@CreatePengaduanActivity, "Gagal memuat kategori: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Kategori>>, t: Throwable) {
                Toast.makeText(this@CreatePengaduanActivity, "Gagal memuat kategori", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupUpload() {
        layoutUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            pickImage.launch(Intent.createChooser(intent, "Pilih gambar"))
        }
    }

    private fun createRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaType())
    }

    private fun uploadPengaduan() {
        val token = getSharedPreferences("auth", MODE_PRIVATE).getString("token", "")
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Token tidak ditemukan, login ulang!", Toast.LENGTH_SHORT).show()
            return
        }

        val judulPart = createRequestBody(etJudul.text.toString())
        val isiPart = createRequestBody(etKeluhan.text.toString())
        val kategoriIdPart = createRequestBody(selectedKategoriId.toString())

        var imagePart: MultipartBody.Part? = null

        selectedImageUri?.let { uri ->
            val fileDescriptor = contentResolver.openFileDescriptor(uri, "r")!!
            val input = FileInputStream(fileDescriptor.fileDescriptor)
            val requestFile = input.readBytes().toRequestBody("image/*".toMediaType())

            imagePart = MultipartBody.Part.createFormData(
                "gambar",
                "upload.jpg",
                requestFile
            )
        }

        ApiClient.instance.createPengaduan(
            "Bearer $token",
            judulPart,
            isiPart,
            kategoriIdPart,
            null,
            null,
            imagePart
        ).enqueue(object : Callback<CreatePengaduanResponse> {
            override fun onResponse(call: Call<CreatePengaduanResponse>, response: Response<CreatePengaduanResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CreatePengaduanActivity, "Berhasil membuat pengaduan!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    // kalau mau, tampilkan errorBody untuk debugging:
                    val err = response.errorBody()?.string()
                    Toast.makeText(this@CreatePengaduanActivity, "Gagal: ${response.code()}", Toast.LENGTH_SHORT).show()
                    // Log.e("API_ERROR", "code=${response.code()} body=$err")
                }
            }

            override fun onFailure(call: Call<CreatePengaduanResponse>, t: Throwable) {
                Toast.makeText(this@CreatePengaduanActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupKirim() {
        btnKirim.setOnClickListener {
            val judul = etJudul.text.toString().trim()
            val keluhan = etKeluhan.text.toString().trim()
            val kategoriNama = autoKategori.text.toString().trim()

            if (judul.isEmpty() || keluhan.isEmpty() || kategoriNama.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            selectedKategoriId = kategoriMap[kategoriNama] ?: 0
            if (selectedKategoriId == 0) {
                Toast.makeText(this, "Kategori tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            uploadPengaduan()
        }
    }

    private fun setupBack() {
        customBackButton.setOnClickListener { finish() }
    }
}

