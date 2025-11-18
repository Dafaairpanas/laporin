package com.smktunas.laporin

import com.google.gson.annotations.SerializedName

data class Kategori(
    @SerializedName("id")
    val id: Int,

    // Gunakan "nama" jika API mengembalikan key "nama".
    // Jika API mengembalikan "name", ganti menjadi:
    // @SerializedName("name") val nama: String
    @SerializedName("nama_kategori")
    val nama_kategori: String
)
