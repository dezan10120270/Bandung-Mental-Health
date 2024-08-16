package com.example.bandungmentalhealthv10.ui.service.FiturCatatanKebaikan

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.ui.service.FiturCatatanKebaikan.Jurnal.JurnalActivity
import com.example.bandungmentalhealthv10.ui.service.FiturCatatanKebaikan.Kalender.KalenderActivity
import com.example.bandungmentalhealthv10.utils.FunPindah

class CatatanKebaikanActivity : AppCompatActivity() {
    private lateinit var kembali: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catatan_kebaikan)

        kembali = findViewById(R.id.kembali)

        kembali.setOnClickListener{
            FunPindah.onBackPressed(this)
            finish()
        }
    }

    fun fKalender(view: View){
        FunPindah.pindahScene(this, KalenderActivity::class.java)
    }

    fun fJurnal(view: View){
        FunPindah.pindahScene(this, JurnalActivity::class.java)
    }
}