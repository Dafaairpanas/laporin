package com.smktunas.laporin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.smktunas.laporin.ui.CreatePengaduanActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PengaduanActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PengaduanAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var emptyStateLayout: LinearLayout

    private var pengaduanList: MutableList<Pengaduan> = mutableListOf()

    companion object {
        const val DETAIL_REQUEST_CODE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaduan)

        // Binding views
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)

        setupBottomNav()

        val token = getSharedPreferences("user_session", MODE_PRIVATE)
            .getString("token", null)

        if (token == null) {
            Toast.makeText(this, "Token tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Load data awal
        getMyPengaduan(token)

        // Pull-to-refresh listener
        swipeRefresh.setOnRefreshListener {
            getMyPengaduan(token)
        }
    }

    private fun getMyPengaduan(token: String) {
        swipeRefresh.isRefreshing = true
        val bearerToken = "Bearer $token"

        ApiClient.instance.getMyPengaduan(bearerToken)
            .enqueue(object : Callback<List<Pengaduan>> {
                override fun onResponse(call: Call<List<Pengaduan>>, response: Response<List<Pengaduan>>) {
                    swipeRefresh.isRefreshing = false
                    pengaduanList.clear()
                    if (response.isSuccessful) {
                        val list = response.body() ?: emptyList()
                        pengaduanList.addAll(list)
                        updateRecyclerView()
                    } else {
                        Toast.makeText(this@PengaduanActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                        updateRecyclerView()
                    }
                }

                override fun onFailure(call: Call<List<Pengaduan>>, t: Throwable) {
                    swipeRefresh.isRefreshing = false
                    Toast.makeText(this@PengaduanActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    updateRecyclerView()
                }
            })
    }

    private fun updateRecyclerView() {
        if (pengaduanList.isEmpty()) {
            recyclerView.visibility = RecyclerView.GONE
            emptyStateLayout.visibility = LinearLayout.VISIBLE
        } else {
            recyclerView.visibility = RecyclerView.VISIBLE
            emptyStateLayout.visibility = LinearLayout.GONE
            if (!::adapter.isInitialized) {
                adapter = PengaduanAdapter(pengaduanList, object : PengaduanAdapter.OnItemClickListener {
                    override fun onEditClick(pengaduan: Pengaduan) {
                        val intent = Intent(this@PengaduanActivity, UpdatePengaduanActivity::class.java)
                        intent.putExtra("id_pengaduan", pengaduan.id)
                        startActivityForResult(intent, DETAIL_REQUEST_CODE)
                    }

                    override fun onDeleteClick(pengaduan: Pengaduan, position: Int) {
                        val token = getSharedPreferences("user_session", MODE_PRIVATE)
                            .getString("token", null) ?: return

                        ApiClient.instance.deletePengaduan("Bearer $token", pengaduan.id)
                            .enqueue(object : Callback<DeleteResponse> {
                                override fun onResponse(call: Call<DeleteResponse>, response: Response<DeleteResponse>) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(this@PengaduanActivity, "Berhasil dihapus", Toast.LENGTH_SHORT).show()
                                        adapter.removeItem(position)
                                    } else {
                                        Toast.makeText(this@PengaduanActivity, "Gagal menghapus", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                                    Toast.makeText(this@PengaduanActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                    }
                })
                recyclerView.adapter = adapter
            } else {
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun setupBottomNav() {
        val navPengaduan = findViewById<LinearLayout>(R.id.nav_pengaduan)
        val navProfil = findViewById<LinearLayout>(R.id.nav_profil)
        val navAdd = findViewById<ImageButton>(R.id.nav_add)

        navPengaduan.setOnClickListener {
            Toast.makeText(this, "Kamu sudah di halaman Pengaduan", Toast.LENGTH_SHORT).show()
        }

        navAdd.setOnClickListener {
            startActivity(Intent(this, CreatePengaduanActivity::class.java))
        }

        navProfil.setOnClickListener {
            startActivity(Intent(this, ProfilActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DETAIL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val token = getSharedPreferences("user_session", MODE_PRIVATE)
                .getString("token", null)
            if (token != null) getMyPengaduan(token)
        }
    }
}
