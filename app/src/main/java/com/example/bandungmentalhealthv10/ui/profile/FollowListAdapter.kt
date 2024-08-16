package com.example.bandungmentalhealthv10.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.data.model.UserData
import com.example.bandungmentalhealthv10.databinding.ItemFollowListBinding
import com.example.bandungmentalhealthv10.utils.hide
import com.example.bandungmentalhealthv10.utils.limitTextLength
import com.example.bandungmentalhealthv10.utils.show

class FollowListAdapter(
    val onItemClicked: (Int, String) -> Unit,
    val onFollowClicked: (Int, String) -> Unit,
    val onUnfollowClicked: (Int, String) -> Unit,
    private val followListStatusListener: FollowListStatusListener
) : RecyclerView.Adapter<FollowListAdapter.FollowListViewHolder>() {

    private var list: MutableList<UserData> = arrayListOf()
    private lateinit var currentUserId: String
    private val taskPerformedSet =
        mutableSetOf<Int>()

    inner class FollowListViewHolder(val binding: ItemFollowListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserData) {
            binding.tvUserName.text = item.userName.limitTextLength()

            binding.ivUserBadge.apply {
                when (item.badge) {
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
            }

            binding.civProfilePicture.apply {
                if (item.profilePicture.isNullOrEmpty()) setImageResource(R.drawable.ic_profile_picture_default)
                else {
                    Glide
                        .with(context)
                        .load(item.profilePicture)
                        .placeholder(R.drawable.ic_profile_picture_default)
                        .error(R.drawable.ic_baseline_error_24)
                        .into(binding.civProfilePicture)
                }
            }

            if (!taskPerformedSet.contains(adapterPosition)) {
                followListStatusListener.isUserBeingFollowed(item.userId, adapterPosition) {
                    if (currentUserId == item.userId) {
                        binding.btFollow.hide()
                        binding.btUnfollow.hide()
                    } else if (it) {
                        binding.btFollow.hide()
                        binding.btUnfollow.show()
                    } else {
                        binding.btFollow.show()
                        binding.btUnfollow.hide()
                    }
                }
                taskPerformedSet.add(adapterPosition)
            }


            binding.clItemFollowList.setOnClickListener {
                onItemClicked.invoke(adapterPosition, item.userId)
            }

            binding.btFollow.setOnClickListener {
                onFollowClicked.invoke(adapterPosition, item.userId)
            }

            binding.btUnfollow.setOnClickListener {
                onUnfollowClicked.invoke(adapterPosition, item.userId)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowListViewHolder {
        val itemView =
            ItemFollowListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FollowListViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(list: MutableList<UserData>) {
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

    interface FollowListStatusListener {
        fun isUserBeingFollowed(item: String, position: Int, callback: (Boolean) -> Unit)
    }


}