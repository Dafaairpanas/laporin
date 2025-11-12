package com.smktunas.laporin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smktunas.laporin.databinding.ActivityDetailBinding

class DetailPengaduan : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Data static
        val title = "A110 MAKAN GAJI BUTA"
        val pengirim = "Imam Batangan IPhone"
        val kategori = "Fasilitas Sekolah"
        val keluhan = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                "Lorem Ipsum has been the industry's standard prabowo gilo pak jokowi gilo sama sama " +
                "gilo dummy text ever since the 1500s, when an unknown printer berat coklat disamping" +
                " adalah 300 gram, salah itu sepertinya took a galley of type and scrambled it to make a " +
                "type specimen book. It has"+"Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                "Lorem Ipsum has been the industry's standard prabowo gilo pak jokowi gilo sama sama " +
                "gilo dummy text ever since the 1500s, when an unknown printer berat coklat disamping" +
                " adalah 300 gram, salah itu sepertinya took a galley of type and scrambled it to make a " +
                "type specimen book. It has"+"Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                "Lorem Ipsum has been the industry's standard prabowo gilo pak jokowi gilo sama sama " +
                "gilo dummy text ever since the 1500s, when an unknown printer berat coklat disamping" +
                " adalah 300 gram, salah itu sepertinya took a galley of type and scrambled it to make a " +
                "type specimen book. It has"+"Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                "Lorem Ipsum has been the industry's standard prabowo gilo pak jokowi gilo sama sama " +
                "gilo dummy text ever since the 1500s, when an unknown printer berat coklat disamping" +
                " adalah 300 gram, salah itu sepertinya took a galley of type and scrambled it to make a " +
                "type specimen book. It has"+"Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                "Lorem Ipsum has been the industry's standard prabowo gilo pak jokowi gilo sama sama " +
                "gilo dummy text ever since the 1500s, when an unknown printer berat coklat disamping" +
                " adalah 300 gram, salah itu sepertinya took a galley of type and scrambled it to make a " +
                "type specimen book. It has"+"Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                "Lorem Ipsum has been the industry's standard prabowo gilo pak jokowi gilo sama sama " +
                "gilo dummy text ever since the 1500s, when an unknown printer berat coklat disamping" +
                " adalah 300 gram, salah itu sepertinya took a galley of type and scrambled it to make a " +
                "type specimen book. It has"+"Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                "Lorem Ipsum has been the industry's standard prabowo gilo pak jokowi gilo sama sama " +
                "gilo dummy text ever since the 1500s, when an unknown printer berat coklat disamping" +
                " adalah 300 gram, salah itu sepertinya took a galley of type and scrambled it to make a " +
                "type specimen book. It has"+"Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                "Lorem Ipsum has been the industry's standard prabowo gilo pak jokowi gilo sama sama " +
                "gilo dummy text ever since the 1500s, when an unknown printer berat coklat disamping" +
                " adalah 300 gram, salah itu sepertinya took a galley of type and scrambled it to make a " +
                "type specimen book. It has"

        // Set data to views
        binding.apply {
            tvTitle.text = title
            tvPengirim.text = pengirim
            tvKategori.text = kategori
            tvKeluhan.text = keluhan

            customBackButton.setOnClickListener {
                finish()
            }
        }
    }
}