package com.example.bandungmentalhealthv10.ui.article

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bandungmentalhealthv10.data.model.ImageListData
import com.example.bandungmentalhealthv10.databinding.ItemCategoryListBinding

class CategoryListAdapter(
    private val categoryList: ArrayList<ImageListData>,
    val onItemClicked: (Int, ImageListData) -> Unit
) : RecyclerView.Adapter<CategoryListAdapter.CategoryListViewHolder>() {

    inner class CategoryListViewHolder(val binding: ItemCategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImageListData) {
            binding.ivCategory.setImageResource(item.image)

            binding.itemCategoryList.setOnClickListener {
                onItemClicked.invoke(adapterPosition, item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        val itemView =
            ItemCategoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        val category = categoryList[position]
        holder.bind(category)
    }

}