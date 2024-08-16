package com.example.bandungmentalhealthv10.ui.post

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.data.model.PostData
import com.example.bandungmentalhealthv10.databinding.ItemPostBinding
import com.example.bandungmentalhealthv10.utils.hide
import com.example.bandungmentalhealthv10.utils.limitTextLength
import com.example.bandungmentalhealthv10.utils.show
import java.text.SimpleDateFormat
import java.util.Locale

class PostAdapter(
    val onOptionClicked: (Int, PostData) -> Unit,
    val onLikeClicked: (Int, String) -> Unit,
    val onCommentClicked: (Int, PostData) -> Unit,
    val onBadgeClicked: (Int, PostData) -> Unit,
    val onProfileClicked: (Int, String) -> Unit,
    private val postStatusListener: PostStatusListener,
    val context: Context
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var list: MutableList<PostData> = arrayListOf()
    private lateinit var currentUserId: String
    private val sdf = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))
    private val taskPerformedSet =
        mutableSetOf<Int>()

    inner class PostViewHolder(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PostData) {
            binding.tvUserNamePost.apply {
                text = if (item.type == "Anonymous") {
                    if (item.createdBy == currentUserId) {
                        "Anonymous (You)"
                    } else {
                        "Anonymous"
                    }

                } else {
                    item.userName.limitTextLength()
                }
            }
            binding.ivUserBadgePost.apply {
                if (item.type == "Anonymous") {
                    hide()
                } else {
                    when (item.userBadge) {
                        "Trusted" -> {
                            setImageResource(R.drawable.ic_badge_trusted)
                        }

                        "Psychologist" -> {
                            setImageResource(R.drawable.ic_badge_psychologist)
                        }

                        else -> {
                            setImageResource(R.drawable.ic_badge_basic)
                        }
                    }
                    show()
                }
            }
            binding.civProfilePicturePost.apply {
                if (item.type == "Anonymous") {
                    setImageResource(R.drawable.ic_profile_picture_anonymous)
                } else {
                    if (item.profilePicture.isNullOrEmpty()) setImageResource(R.drawable.ic_profile_picture_default)
                    else {
                        Glide
                            .with(context)
                            .load(item.profilePicture)
                            .placeholder(R.drawable.ic_profile_picture_default)
                            .error(R.drawable.ic_baseline_error_24)
                            .into(binding.civProfilePicturePost)
                    }
                }
            }

            binding.tvCategoryPost.text = item.category
            binding.tvTimePost.text = sdf.format(item.createdAt)
            binding.tvCaptionPost.text = item.caption
            binding.tvLikeCountPost.text = item.likesCount.toString()
            binding.tvCommentCountPost.text = item.commentsCount.toString()
            if (item.image == null) {
                binding.cvImagePost.hide()
            } else {
                binding.cvImagePost.show()
                Glide
                    .with(context)
                    .load(item.image)
                    .placeholder(R.drawable.shape_image_content)
                    .error(R.drawable.ic_baseline_error_24)
                    .into(binding.ivImagePost)
            }

            if (!taskPerformedSet.contains(adapterPosition)) {
                postStatusListener.isPostBeingLiked(item.postId, adapterPosition) {
                    if (it) {
                        binding.ivLikeButtonPost.setImageResource(R.drawable.ic_like_solid)
                    } else {
                        binding.ivLikeButtonPost.setImageResource(R.drawable.ic_like_light)
                    }
                }
                taskPerformedSet.add(adapterPosition)
            }

            binding.ivLikeButtonPost.setOnClickListener {
                onLikeClicked.invoke(adapterPosition, item.postId)
            }
            binding.ivCommentButtonPost.setOnClickListener {
                onCommentClicked.invoke(adapterPosition, item)
            }
            binding.ivPostOptions.setOnClickListener {
                onOptionClicked.invoke(adapterPosition, item)
            }
            binding.ivUserBadgePost.setOnClickListener {
                onBadgeClicked.invoke(adapterPosition, item)
            }
            binding.civProfilePicturePost.setOnClickListener {
                if (item.type != "Anonymous") onProfileClicked.invoke(
                    adapterPosition,
                    item.createdBy
                )
            }
            binding.tvUserNamePost.setOnClickListener {
                if (item.type != "Anonymous") onProfileClicked.invoke(
                    adapterPosition,
                    item.createdBy
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(list: MutableList<PostData>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun updateCurrentUser(userId: String) {
        this.currentUserId = userId
    }

    fun dataUpdated(position: Int) {
        taskPerformedSet.remove(position)
        notifyItemChanged(position)
    }

    fun updateLikeCount(position: Int, value: Int) {
        list[position].likesCount = list[position].likesCount + value
        notifyItemChanged(position, list[position])
    }

    fun updateCommentCount(position: Int, value: Int) {
        list[position].commentsCount = list[position].commentsCount + value
        notifyItemChanged(position, list[position])
    }

    fun clearData() {
        taskPerformedSet.clear()
    }

    interface PostStatusListener {
        fun isPostBeingLiked(item: String, position: Int, callback: (Boolean) -> Unit)
    }

}