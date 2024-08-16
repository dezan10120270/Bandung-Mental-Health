package com.example.bandungmentalhealthv10.ui.createpost

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.databinding.ActivitySelectCategoryBinding
import com.example.bandungmentalhealthv10.databinding.ItemChipCategoryBinding
import com.google.android.material.chip.Chip

class SelectCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectCategoryBinding
    private lateinit var selectedCategory: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
        setStatusBarColor()
        setupChip()

        binding.chipGroupCategory.setOnCheckedStateChangeListener { group, _ ->
            val selectedChip = group.findViewById<Chip>(group.checkedChipId)
            selectedCategory = selectedChip.text.toString()
            binding.btNext.isEnabled = true
        }

        binding.btNext.setOnClickListener {
            val intent = Intent(this@SelectCategoryActivity, CreatePostActivity::class.java)
            intent.putExtra("SELECTED_CATEGORY", selectedCategory)
            startActivity(intent)
        }

    }

    private fun setupChip() {
        val categoryList =
            arrayListOf(
                "Kesehatan Mental",
                "Perundungan Fisik",
                "Depresi",
                "Perundungan",
                "Pelecehan",
                "Kekerasan",
                "Kecanduan",
                "Kesehatan",
                "Pekerjaan",
                "Pendidikan",
                "Keluarga",
                "Kecemasan",
                "Kepribadian",
                "Diskriminasi",
                "Rasis",
                "Teman",
                "Sara",
                "Hubungan",
                "Keuangan",
                "Traumatis",
                "Lainnya"
            )
        for (name in categoryList) {
            val chip = createChip(name)
            binding.chipGroupCategory.addView(chip)
        }
    }

    private fun createChip(label: String): Chip {
        val chip = ItemChipCategoryBinding.inflate(layoutInflater).root
        chip.text = label
        return chip
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}