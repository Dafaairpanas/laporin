package com.smktunas.laporin

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ButtonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_button)

        val pengaduanBtn = findViewById<LinearLayout>(R.id.nav_pengaduan)
        val profilBtn = findViewById<LinearLayout>(R.id.nav_profil)

        pengaduanBtn.isSelected = true
        profilBtn.isSelected = false

        pengaduanBtn.setOnClickListener {
            pengaduanBtn.isSelected = true
            profilBtn.isSelected = false
//            val intent = Intent(this, ButtonActivity::class.java)
//            startActivity(intent)
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        profilBtn.setOnClickListener {
            pengaduanBtn.isSelected = false
            profilBtn.isSelected = true
//            val intent = Intent(this, Button2Activity::class.java)
//            startActivity(intent)
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
