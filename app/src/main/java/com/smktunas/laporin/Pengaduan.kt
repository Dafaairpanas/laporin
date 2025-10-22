package com.smktunas.laporin

data class Pengaduan(
    val id: Int,
    val judul: String,
    val deskripsi: String,
    val kategori: String,
    val status: String,
    val gambar: String?
)
