package com.example.bandungmentalhealthv10.ui.article

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.data.model.CategoryContentData
import com.example.bandungmentalhealthv10.databinding.ItemVideoBinding
import java.text.SimpleDateFormat
import java.util.Locale

class VideoAdapter(
    val onItemClick: (Int, CategoryContentData) -> Unit,
    val context: Context
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private var list: MutableList<CategoryContentData> = arrayListOf()
    val sdf = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))

    inner class VideoViewHolder(val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CategoryContentData) {
            binding.tvTitleVideo.text = item.title
            binding.tvSourceVideo.text = item.source
            binding.tvTimeCreated.text = sdf.format(item.publishedAt)

            binding.itemVideo.setOnClickListener {
                onItemClick.invoke(adapterPosition, item)
            }

            if (item.image == "null") {
                binding.ivImageVideo.setImageResource(R.drawable.shape_image_content)
            } else {
                Glide
                    .with(context)
                    .load(item.image)
                    .placeholder(R.drawable.shape_image_content)
                    .error(R.drawable.ic_baseline_error_24)
                    .into(binding.ivImageVideo)
            }

            binding.tvVideoDuration.text = item.duration
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val itemView = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
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