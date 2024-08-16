package com.example.bandungmentalhealthv10.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.core.content.res.ResourcesCompat
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.databinding.ActivityLoginOptionsBinding

class LoginOptionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginOptionsBinding
    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                NavUtils.navigateUpFromSameTask(this@LoginOptionsActivity)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        setToolbar()
        setStatusBarColor()

        binding.btLoginEmail.setOnClickListener {
            val intent = Intent(this, LoginWithEmailActivity::class.java)
            startActivity(intent)
        }
        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, SignupOptionsActivity::class.java)
            startActivity(intent)
        }
        binding.btLoginGoogle.setOnClickListener {
            Toast.makeText(application, "Sedang dalam perbaikan", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_blue)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setStatusBarColor() {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ResourcesCompat.getColor(resources, R.color.BlueBackground, null)
        window.decorView.systemUiVisibility = 0
    }

}