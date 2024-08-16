package com.example.bandungmentalhealthv10.ui.authentication

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.example.bandungmentalhealthv10.data.model.UserData
import com.example.bandungmentalhealthv10.databinding.ActivitySignupWithEmailBinding
import com.example.bandungmentalhealthv10.ui.MainActivity
import com.example.bandungmentalhealthv10.utils.UiState
import com.example.bandungmentalhealthv10.utils.capitalizeWords
import com.example.bandungmentalhealthv10.utils.hide
import com.example.bandungmentalhealthv10.utils.hideKeyboard
import com.example.bandungmentalhealthv10.utils.show
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class SignupWithEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupWithEmailBinding
    private val viewModel: AuthViewModel by viewModels()
    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                NavUtils.navigateUpFromSameTask(this@SignupWithEmailActivity)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupWithEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        setToolbar()

        observer()

        binding.btSignUp.setOnClickListener {
            val email: String = binding.etEmail.text.toString().trim()
            val pass: String = binding.etPassword.text.toString()
            val name: String = binding.etName.text.toString().trim()

            if (signupValidation(email, pass, name)) {
                viewModel.register(
                    email = email,
                    password = pass,
                    user = getUserObj()
                )
            }

            hideKeyboard()
        }
    }

    private fun observer() {
        viewModel.register.observe(this) { state ->
            when (state) {
                is UiState.Failure -> {
                    loadingVisibility(false)
                    Snackbar.make(binding.root, state.error.toString(), Snackbar.LENGTH_SHORT)
                        .show()
                }

                UiState.Loading -> {
                    loadingVisibility(true)
                }

                is UiState.Success -> {
                    loadingVisibility(false)
                    val intent = Intent(this@SignupWithEmailActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra("SIGNUP_SUCCESSFUL", "true")
                    startActivity(intent)
                }
            }
        }
    }

    private fun getUserObj(): UserData {
        return UserData(
            userId = "",
            userName = binding.etName.text.toString().capitalizeWords().trim(),
            createdAt = Date(),
            bio = null,
            profilePicture = null,
            email = binding.etEmail.text.toString().trim(),
            gender = null,
            birthDate = null,
            badge = "Basic"
        )
    }

    private fun signupValidation(email: String, pass: String, name: String): Boolean {
        return if (email.isEmpty()) {
            binding.etEmail.error = "Silahkan masukkan email"
            false
        } else if (name.isEmpty()) {
            binding.etName.error = "Silahkan masukan nama anda"
            false
        } else if (pass.isEmpty()) {
            binding.etPassword.error = "Silahkan masukkan password"
            false
        } else if (!isOnlyLettersAndSpace(name)) {
            binding.etName.error = "Masukan huruf saja"
            false
        } else {
            if (isValidPassword(pass)) {
                true
            } else {
                binding.etPassword.error = "harus lebih dari 8 karakter, berisi angka, " +
                        "simbol dan huruf besar"
                false
            }
        }
    }

    private fun loadingVisibility(isLoading: Boolean) {
        if (isLoading) {
            binding.tvSignup.hide()
            binding.progressBar.show()
        } else {
            binding.tvSignup.show()
            binding.progressBar.hide()
        }
    }

    private fun isOnlyLettersAndSpace(name: String): Boolean {
        var x = true
        for (i in name) {
            if (!i.isLetter() && !i.isWhitespace()) {
                x = false
                break
            }
        }
        return x
    }

    private fun isValidPassword(pass: String): Boolean {
        if (pass.length < 8) return false
        if (pass.firstOrNull { it.isDigit() } == null) return false
        if (pass.filter { it.isLetter() }.firstOrNull { it.isUpperCase() } == null) return false
        if (pass.filter { it.isLetter() }.firstOrNull { it.isLowerCase() } == null) return false
        if (pass.firstOrNull { !it.isLetterOrDigit() } == null) return false

        return true
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.example.bandungmentalhealthv10.R.drawable.ic_back_grey)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

}