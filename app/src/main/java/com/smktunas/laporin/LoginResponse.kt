package com.smktunas.laporin

data class LoginResponse(
    val status: Boolean? = null,
    val message: String? = null,
    val token: String? = null,
    val user: User? = null
)

data class User(
    val id: Int? = null,
    val name: String? = null,
    val email: String? = null
)
