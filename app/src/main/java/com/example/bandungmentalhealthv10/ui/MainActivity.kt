package com.example.bandungmentalhealthv10.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.databinding.ActivityMainBinding
import com.example.bandungmentalhealthv10.ui.createpost.SelectCategoryActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var justSignUp = false
    private var justLogIn = false
    private var justPublishedPost = false
    private var justPublishedDiary = false
    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.bottomView.selectedItemId == R.id.forumFragment) {
                val a = Intent(Intent.ACTION_MAIN)
                a.addCategory(Intent.CATEGORY_HOME)
                a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(a)
            } else {
                binding.bottomView.selectedItemId = R.id.forumFragment
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        val navController = findNavController(R.id.fragment)
        binding.bottomView.setupWithNavController(navController)

        justLogIn = intent.getStringExtra("LOGIN_SUCCESSFUL").toBoolean()
        justSignUp = intent.getStringExtra("SIGNUP_SUCCESSFUL").toBoolean()
        justPublishedPost = intent.getStringExtra("PUBLISH_POST_SUCCESSFUL").toBoolean()
        justPublishedDiary = intent.getStringExtra("PUBLISH_DIARY_SUCCESSFUL").toBoolean()
        showMessageDialog()

        binding.fabCreatePost.setOnClickListener {
            val intent = Intent(this, SelectCategoryActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showMessageDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_successful_message)

        val title = dialog.findViewById<TextView>(R.id.tvTitle)
        val description = dialog.findViewById<TextView>(R.id.tvDescription)

        if (justLogIn) {
            title.setText(R.string.login_success_title)
            description.setText(R.string.login_success_description)
            dialog.show()
        }

        if (justSignUp) {
            title.setText(R.string.signup_success_title)
            description.setText(R.string.signup_success_description)
            dialog.show()
        }

        if (justPublishedPost) {
            dialog.setContentView(R.layout.dialog_published_post_successful)
            dialog.show()
        }

        if (justPublishedDiary) {
            dialog.setContentView(R.layout.dialog_published_post_successful)
            val title = dialog.findViewById<TextView>(R.id.tvTitle)
            val description = dialog.findViewById<TextView>(R.id.tvDescription)
            title.text = "Diary berhasil disimpan!"
            description.text = "Diary Anda berhasil disimpan"
            dialog.show()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
        }, 2000)
    }

}