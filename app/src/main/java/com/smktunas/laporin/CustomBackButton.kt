package com.smktunas.laporin

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.smktunas.laporin.R

class CustomBackButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private val tvButtonText: TextView
    private val ivIcon: ImageView

    init {
        // Inflate layout XML untuk isi tombol
        LayoutInflater.from(context).inflate(R.layout.view_back_button, this, true)

        tvButtonText = findViewById(R.id.tvButtonText)
        ivIcon = findViewById(R.id.ivIcon)

        // Styling CardView utama
        radius = 85f
        cardElevation = 8f
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.darkGrey)) // gunakan warna dari colors.xml

        // Klikable + ripple
        isClickable = true
        isFocusable = true

        // Hindari padding ganda di sudut
        preventCornerOverlap = false
        useCompatPadding = true
    }

    fun setText(text: String) {
        tvButtonText.text = text
    }
}
