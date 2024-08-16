package com.example.bandungmentalhealthv10.ui.service.FiturKonsultasi

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.databinding.ActivityKonsultasiBinding
import com.example.bandungmentalhealthv10.ui.jadwal.SuccesActivity
import com.example.bandungmentalhealthv10.utils.FunPindah
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import java.util.UUID

class KonsultasiActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    // Deklarasi Variabel
    var jam = 0
    var menit = 0
    var tanggal = 0
    var bulan = 0
    var tahun = 0

    // lateinit digunakan untuk membuat inisialisasi diakhir
    lateinit var teksTanggal: TextView
    lateinit var teksWaktu: TextView

    private lateinit var binding: ActivityKonsultasiBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Proses Inisialisasi xml
        binding = ActivityKonsultasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendeklarasikan TextView
        val teksDok1: TextView = findViewById(R.id.textDokter)
        val teksDok2: TextView = findViewById(R.id.textDokter2)
        val textView2: TextView = findViewById(R.id.textView)

        // Menambahkan OnClickListener ke TextView
        teksDok1.setOnClickListener {
            // Menetapkan teks yang diinginkan saat TextView ditekan
            textView2.text = "Dr. Seto,  S.Psi., M.Psi"
        }
        teksDok2.setOnClickListener {
            // Menetapkan teks yang diinginkan saat TextView ditekan
            textView2.text = "Dr. Melya Putri, S.Psi"
        }

        binding.kembali.setOnClickListener {
            FunPindah.onBackPressed(this)
        }

        // Tombol Simpan dan Proses Simpan ke dalam database Firebase Realtime
        binding.saveButton.setOnClickListener {

            // Proses Konfigurasi Ui dan Controller
            val id = UUID.randomUUID().toString()
            val namaDokter = binding.textView.text.toString()
            val sesiTanggal = binding.uploadTanggal.text.toString()
            val sesiWaktu = binding.uploadWaktu.text.toString()
            val deskMasalah = binding.uploadMasalah.text.toString()

            // Tentukan Path Nama database
            database = FirebaseDatabase.getInstance().getReference("Jadwal Konsultasi")
            // Konfigurasi penyimpanan data ke dalam database menggunakan data jadwal
            val users = Konsultasi(id, namaDokter, sesiTanggal, sesiWaktu, deskMasalah)
            database.child(id).setValue(users).addOnSuccessListener {

                // Toast berhasil
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SuccesActivity::class.java)
                startActivity(intent)
                finish()

            }.addOnFailureListener {
                //toast gagal
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getSaatIni(){
        val kal: Calendar = Calendar.getInstance()
        tanggal = kal.get(Calendar.DAY_OF_MONTH)
        bulan = kal.get(Calendar.MONTH)
        tahun = kal.get(Calendar.YEAR)
        jam = kal.get(Calendar.HOUR_OF_DAY)
        menit = kal.get(Calendar.MINUTE)
    }

    fun fSetTanggal(view: View){
        getSaatIni()
        DatePickerDialog(this, this,  tahun, bulan, tanggal).show()
    }

    fun fSetWaktu(view: View){
        getSaatIni()
        TimePickerDialog(this, this, jam, menit, true).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        tanggal = dayOfMonth
        bulan = month + 1 // Tambahkan 1 pada indeks bulan
        tahun = year

        // findviewbyid untuk mengintisiasi dengan file xml
        teksTanggal = findViewById(R.id.uploadTanggal)
        teksTanggal.text = "${tanggal} - ${bulan} - ${tahun}"
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        jam = hourOfDay
        menit = minute

        teksWaktu = findViewById(R.id.uploadWaktu)
        teksWaktu.text = "${jam}:${menit}"
    }

}