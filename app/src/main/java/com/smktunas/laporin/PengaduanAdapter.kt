package com.smktunas.laporin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class PengaduanAdapter(private val data: List<Pengaduan>) :
    RecyclerView.Adapter<PengaduanAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val judul = itemView.findViewById<TextView>(R.id.tvJudul)
        val kategori = itemView.findViewById<TextView>(R.id.tvKategori)
        val status = itemView.findViewById<TextView>(R.id.tvStatus)
        val gambar = itemView.findViewById<ImageView>(R.id.ivGambar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pengaduan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.judul.text = item.judul
        holder.kategori.text = item.kategori
        holder.status.text = item.status

        Glide.with(holder.itemView.context)
            .load("http://192.168.2.4:8000/storage/${item.gambar}")
            .into(holder.gambar)
    }

    override fun getItemCount() = data.size
}
