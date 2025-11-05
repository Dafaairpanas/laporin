package com.smktunas.laporin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val title = findViewById<TextView>(R.id.titleText)
        val text = "Laporin!"
        val handler = Handler(Looper.getMainLooper())

        var displayed = ""
        val delayPerLetter = 100L // makin kecil = makin cepat

        // huruf muncul satu per satu tanpa animasi tambahan
        for (i in text.indices) {
            handler.postDelayed({
                displayed += text[i]
                title.text = displayed
            }, i * delayPerLetter)
        }

        // total waktu tampil huruf + delay sebelum lanjut ke LoginActivity
        val totalDelay = (text.length * delayPerLetter) + 1000

        handler.postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, totalDelay)
    }
}
