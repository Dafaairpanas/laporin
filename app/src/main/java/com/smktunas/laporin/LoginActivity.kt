package com.smktunas.laporin

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val emailInput = etEmail.text.toString()
            val passwordInput = etPassword.text.toString()

            if (emailInput.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(this, "Email dan password wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ApiClient.instance.login(emailInput, passwordInput)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful && response.body()?.token != null) {
                            val token = response.body()?.token
                            val user = response.body()?.user
                            val name = user?.name ?: "User"
                            val email = user?.email ?: "user@example.com"

                            // Simpan ke SharedPreferences
                            val pref = getSharedPreferences("user_session", MODE_PRIVATE)
                            pref.edit()
                                .putString("token", token)
                                .putString("name", name)
                                .putString("email", email)
                                .apply()

                            // Lanjut ke PengaduanActivity
                            startActivity(Intent(this@LoginActivity, PengaduanActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Login gagal", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        val buatAkun = findViewById<TextView>(R.id.tvLinkRegister)
        buatAkun.setOnClickListener {
            startActivity(Intent(this, DaftarActivity::class.java))
        }
    }
}
