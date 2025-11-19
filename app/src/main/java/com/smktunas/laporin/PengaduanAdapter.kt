package com.smktunas.laporin

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PengaduanAdapter(
    private var data: MutableList<Pengaduan>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PengaduanAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onEditClick(pengaduan: Pengaduan)
        fun onDeleteClick(pengaduan: Pengaduan, position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val judul: TextView = itemView.findViewById(R.id.tvJudul)
        val kategori: TextView = itemView.findViewById(R.id.tvKategori)
        val status: TextView = itemView.findViewById(R.id.tvStatus)
        val gambar: ImageView = itemView.findViewById(R.id.ivGambar)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pengaduan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.judul.text = item.judul ?: "(Tanpa judul)"
        holder.kategori.text = item.kategori?.nama_kategori ?: "Tidak ada kategori"
        holder.status.text = item.status ?: "-"

        val imageUrl = "http://192.168.3.13:8000/storage/${item.gambar}"
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.gambar)

        // Klik item untuk detail
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailPengaduan::class.java)
            intent.putExtra("id_pengaduan", item.id)
            (context as Activity).startActivityForResult(intent, PengaduanActivity.DETAIL_REQUEST_CODE)
        }

        // Tombol Edit
        holder.btnEdit.setOnClickListener {
            listener.onEditClick(item)
        }

        // Tombol Delete
        holder.btnDelete.setOnClickListener {
            listener.onDeleteClick(item, position)
        }
    }

    override fun getItemCount() = data.size

    fun removeItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateList(newList: List<Pengaduan>) {
        data.clear()
        data.addAll(newList)
        notifyDataSetChanged()
    }
}
