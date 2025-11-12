package com.smktunas.laporin

data class Pengaduan(
    val id: Int,
    val user_id: Int?,
    val judul: String?,
    val isi: String?,
    val gambar: String?,
    val kategori_id: Int?,
    val kelas_id: Int?,
    val is_anonymous: Int?,
    val status: String?,
    val created_at: String?,
    val updated_at: String?,
    val kategori: Kategori?,
    val kelas: Kelas?
)

data class Kategori(
    val id: Int?,
    val nama_kategori: String?,
    val deskripsi: String?
)

data class Kelas(
    val id: Int?,
    val id_gedung: Int?,
    val nama_kelas: String?,
    val pic: String?,
    val layout: String?,
    val standar_operasional: String?
)