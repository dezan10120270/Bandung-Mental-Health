package com.example.bandungmentalhealthv10.ui.article

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.data.model.CategoryContentData
import com.example.bandungmentalhealthv10.databinding.ItemArticleBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ArticleAdapter(
    val onItemClick: (Int, CategoryContentData) -> Unit,
    val context: Context
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    private var list: MutableList<CategoryContentData> = arrayListOf()
    val sdf = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))

    inner class ArticleViewHolder(val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CategoryContentData) {
            binding.tvTitleArticle.text = item.title
            binding.tvSourceArticle.text = item.source
            binding.tvTimeCreated.text = sdf.format(item.publishedAt)

            binding.itemArticle.setOnClickListener {
                onItemClick.invoke(adapterPosition, item)
            }

            if (item.image == "null") {
                binding.ivImageArticle.setImageResource(R.drawable.shape_image_content)
            } else {
                Glide
                    .with(context)
                    .load(item.image)
                    .placeholder(R.drawable.shape_image_content)
                    .error(R.drawable.ic_baseline_error_24)
                    .into(binding.ivImageArticle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val itemView =
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(list: MutableList<CategoryContentData>) {
        this.list = list
        notifyDataSetChanged()
    }

}