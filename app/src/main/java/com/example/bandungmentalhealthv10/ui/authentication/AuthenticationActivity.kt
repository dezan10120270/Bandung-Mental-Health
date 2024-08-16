package com.example.bandungmentalhealthv10.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.databinding.ActivityAuthenticationBinding
import com.example.bandungmentalhealthv10.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarColor()

        binding.tvLoginButton.setOnClickListener {
            val intent = Intent(this, LoginOptionsActivity::class.java)
            startActivity(intent)
        }

        binding.btSignup.setOnClickListener {
            val intent = Intent(this, SignupOptionsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setStatusBarColor() {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ResourcesCompat.getColor(resources, R.color.BlueBackground, null)
        window.decorView.systemUiVisibility = 0
    }

    override fun onStart() {
        super.onStart()

        viewModel.getSession { user ->
            if (user != null) {
                val intent = Intent(this@AuthenticationActivity, MainActivity::class.java)
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }
}