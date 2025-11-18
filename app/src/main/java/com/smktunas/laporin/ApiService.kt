package com.smktunas.laporin

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("kategori")
    fun getKategori(): Call<List<Kategori>>


    @GET("pengaduan")
    fun getPengaduan(): Call<List<Pengaduan>>

    @GET("pengaduan/me")
    fun getMyPengaduan(
        @Header("Authorization") token: String
    ): Call<List<Pengaduan>>

    @Multipart
    @POST("pengaduan")
    fun createPengaduan(
        @Header("Authorization") token: String,
        @Part("judul") judul: RequestBody,
        @Part("isi") isi: RequestBody,
        @Part("kategori_id") kategoriId: RequestBody,
        @Part("kelas_id") kelasId: RequestBody?, // optional
        @Part("is_anonymous") isAnonymous: RequestBody?, // optional
        @Part gambar: MultipartBody.Part? = null
    ): Call<CreatePengaduanResponse>



}
