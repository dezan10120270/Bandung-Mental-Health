package com.example.bandungmentalhealthv10.ui.article

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.data.model.ImageListData
import com.example.bandungmentalhealthv10.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {

    lateinit var binding: FragmentArticleBinding
    lateinit var categoryList: ArrayList<ImageListData>
    lateinit var categoryListAdapter: CategoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCategoryList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvCategoryList.setHasFixedSize(true)
        binding.rvCategoryList.isNestedScrollingEnabled = false

        categoryList = ArrayList()
        addCategoryListData()

        categoryListAdapter =
            CategoryListAdapter(
                categoryList,
                onItemClicked = { pos, item ->
                    val intent = Intent(requireContext(), CategoryContentActivity::class.java)
                    intent.putExtra("CATEGORY", item.name)
                    startActivity(intent)
                }
            )

        binding.rvCategoryList.adapter = categoryListAdapter
    }

    private fun addCategoryListData() {
        categoryList.add(ImageListData(R.drawable.il_category_mentalhealth, "Kesehatan Mental"))
        categoryList.add(ImageListData(R.drawable.il_category_relationship, "Hubungan"))
        categoryList.add(ImageListData(R.drawable.il_category_family, "Keluarga"))
        categoryList.add(ImageListData(R.drawable.il_category_health, "Kesehatan"))
        categoryList.add(ImageListData(R.drawable.il_category_abuse, "Kekerasan"))
        categoryList.add(ImageListData(R.drawable.il_cateogry_bullying, "Perundungan"))
        categoryList.add(ImageListData(R.drawable.il_category_sara, "Sara"))
        categoryList.add(ImageListData(R.drawable.il_category_depression, "Depresi"))
        categoryList.add(ImageListData(R.drawable.il_category_harassment, "Pelecehan"))
        categoryList.add(ImageListData(R.drawable.il_category_addictioin, "Kecanduan"))
        categoryList.add(ImageListData(R.drawable.il_category_work, "Pekerjaan"))
        categoryList.add(ImageListData(R.drawable.il_category_education, "Pendidikan"))
        categoryList.add(ImageListData(R.drawable.il_category_personality, "Kepribadian"))
        categoryList.add(ImageListData(R.drawable.il_category_bodyshaming, "Perundungan Fisik"))
        categoryList.add(ImageListData(R.drawable.il_category_anxiety, "Kecemasan"))
        categoryList.add(ImageListData(R.drawable.il_category_friends, "Teman"))
        categoryList.add(ImageListData(R.drawable.il_category_traumatic, "Traumatis"))
        categoryList.add(ImageListData(R.drawable.il_category_financial, "Keuangan"))
        categoryList.add(ImageListData(R.drawable.il_category_selfharm, "Menyakiti Diri"))
        categoryList.add(ImageListData(R.drawable.il_category_discrimination, "Diskriminasi"))
    }
}