package com.example.bandungmentalhealthv10.ui.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.data.model.CommentData
import com.example.bandungmentalhealthv10.data.model.PostData
import com.example.bandungmentalhealthv10.databinding.ItemCommentBinding
import com.example.bandungmentalhealthv10.utils.hide
import com.example.bandungmentalhealthv10.utils.limitTextLength
import com.example.bandungmentalhealthv10.utils.show
import java.text.SimpleDateFormat
import java.util.Locale

class CommentAdapter(
    val onOptionsCommentClicked: (Int, CommentData) -> Unit,
    val onProfileClicked: (Int, String) -> Unit,
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private var list: MutableList<CommentData> = arrayListOf()
    private lateinit var objectPost: PostData
    private lateinit var currentUserId: String
    val sdf = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))

    inner class CommentViewHolder(val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CommentData) {
            binding.tvUserNameComment.apply {
                text =
                    if (objectPost.type == "Anonymous" && objectPost.createdBy == item.commentedBy && item.commentedBy == currentUserId) {
                        "Anonymous (You)"
                    } else if (objectPost.type == "Anonymous" && objectPost.createdBy == item.commentedBy) {
                        "Anonymous (Post Owner)"
                    } else {
                        item.userName.limitTextLength()
                    }
            }

            binding.tvTimeComment.text = sdf.format(item.commentedAt)
            binding.tvComment.text = item.comment

            binding.ivCommentOptions.setOnClickListener {
                onOptionsCommentClicked.invoke(adapterPosition, item)
            }

            binding.ivUserBadgeComment.apply {
                if (objectPost.type == "Anonymous" && objectPost.createdBy == item.commentedBy) {
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

            binding.ivCommentOptions.setOnClickListener {
                onOptionsCommentClicked.invoke(adapterPosition, item)
            }

            binding.civProfilePictureComment.setOnClickListener {
                if (!(objectPost.type == "Anonymous" && objectPost.createdBy == item.commentedBy))
                    onProfileClicked.invoke(adapterPosition, item.commentedBy)
            }

            binding.tvUserNameComment.setOnClickListener {
                if (!(objectPost.type == "Anonymous" && objectPost.createdBy == item.commentedBy))
                    onProfileClicked.invoke(adapterPosition, item.commentedBy)
            }

            binding.civProfilePictureComment.apply {
                if (objectPost.type == "Anonymous" && objectPost.createdBy == item.commentedBy) {
                    setImageResource(R.drawable.ic_profile_picture_anonymous)
                } else {
                    if (item.profilePicture.isNullOrEmpty()) setImageResource(R.drawable.ic_profile_picture_default)
                    else {
                        Glide
                            .with(context)
                            .load(item.profilePicture)
                            .placeholder(R.drawable.ic_profile_picture_default)
                            .error(R.drawable.ic_baseline_error_24)
                            .into(binding.civProfilePictureComment)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView =
            ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(list: MutableList<CommentData>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun updatePostType(objectPost: PostData) {
        this.objectPost = objectPost
    }

    fun updateCurrentUser(userId: String) {
        this.currentUserId = userId
    }

}