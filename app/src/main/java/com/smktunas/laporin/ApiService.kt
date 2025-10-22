package com.smktunas.laporin

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("pengaduan")
    fun getPengaduan(): Call<List<Pengaduan>>

    @POST("login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("pengaduan/me")
    fun getMyPengaduan(
        @Header("Authorization") token: String
    ): Call<List<Pengaduan>>
}

data class LoginResponse(
    val token: String?,
    val user: User?
)

data class User(
    val id: Int,
    val name: String
)
