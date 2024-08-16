package com.example.bandungmentalhealthv10.ui.jadwal

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.ui.MainActivity
import com.example.bandungmentalhealthv10.utils.FunPindah

class SuccesActivity : AppCompatActivity() {
    private val delayMillis: Long = 3000 // 3 detik

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_succes)

        // Handler untuk menunda eksekusi kode selama delayMillis
        Handler().postDelayed({
            // Set the default fragment
            FunPindah.pindahScene(this, MainActivity::class.java)
        }, delayMillis)
    }

}