package com.example.bandungmentalhealthv10.ui.forum

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.data.model.LikeData
import com.example.bandungmentalhealthv10.data.model.PostData
import com.example.bandungmentalhealthv10.data.model.UserData
import com.example.bandungmentalhealthv10.databinding.FragmentForumBinding
import com.example.bandungmentalhealthv10.ui.comment.CommentActivity
import com.example.bandungmentalhealthv10.ui.post.PostAdapter
import com.example.bandungmentalhealthv10.ui.post.PostOptionsBottomSheetFragment
import com.example.bandungmentalhealthv10.ui.post.PostViewModel
import com.example.bandungmentalhealthv10.ui.profile.VisitedProfileActivity
import com.example.bandungmentalhealthv10.utils.UiState
import com.example.bandungmentalhealthv10.utils.getFirstWord
import com.example.bandungmentalhealthv10.utils.hide
import com.example.bandungmentalhealthv10.utils.limitTextLength
import com.example.bandungmentalhealthv10.utils.show
import com.example.bandungmentalhealthv10.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class ForumFragment :
    Fragment(),
    PostOptionsBottomSheetFragment.PostOptionsStatusListener,
    PostAdapter.PostStatusListener {

    lateinit var binding: FragmentForumBinding
    private val viewModel: PostViewModel by viewModels()
    private var selectedType = "All"
    private var postList: MutableList<PostData> = arrayListOf()
    private var currentItemPosition = -1
    private val adapter by lazy {
        PostAdapter(
            onCommentClicked = { pos, item ->
                val intent = Intent(requireContext(), CommentActivity::class.java)
                intent.putExtra("OBJECT_POST", item)
                currentItemPosition = pos
                resultLauncher.launch(intent)
            },
            onLikeClicked = { pos, item ->
                addLike(item, pos)
            },
            onOptionClicked = { pos, item ->
                val postOptionsBottomDialogFragment = PostOptionsBottomSheetFragment(this)

                val bundle = Bundle()
                bundle.putParcelable("OBJECT_POST", item)
                currentItemPosition = pos
                postOptionsBottomDialogFragment.arguments = bundle

                postOptionsBottomDialogFragment.show(
                    childFragmentManager,
                    "PostOptionsBottomSheetFragment"
                )

            },
            onBadgeClicked = { pos, item ->

            },
            onProfileClicked = { _, item ->
                if (item == currentUserData().userId) {
                } else {
                    val intent = Intent(requireContext(), VisitedProfileActivity::class.java)
                    intent.putExtra("USER_ID", item)
                    startActivity(intent)
                }
            },
            postStatusListener = this,
            context = requireContext()
        )
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val commentAddedAmount = result.data?.getIntExtra("COMMENT_ADDED", 0)
                commentAddedAmount?.let { updateCommentCount(it) }
            } else if (result.resultCode == 101) {
                viewModel.getPosts(selectedType, currentUserData().userId)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForumBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMessage()

        binding.srlHomeFragment.setOnRefreshListener {
            viewModel.getPosts(selectedType, currentUserData().userId)
        }

        binding.btYouLiked.setOnClickListener {
            val intent = Intent(requireContext(), UserLikedActivity::class.java)
            resultLauncher.launch(intent)
        }

        binding.chipForYou.setOnClickListener {
            selectedType = "All"
            viewModel.getPosts(selectedType, currentUserData().userId)
        }
        binding.chipFollowing.setOnClickListener {
            selectedType = "Following"
            viewModel.getPosts(selectedType, currentUserData().userId)
        }
        binding.chipSpeakUp.setOnClickListener {
            selectedType = "Anonymous"
            viewModel.getPosts(selectedType, currentUserData().userId)
        }

        binding.rvPost.adapter = adapter
        binding.rvPost.layoutManager = LinearLayoutManager(context)
        binding.rvPost.setHasFixedSize(true)
        binding.rvPost.isNestedScrollingEnabled = false

        viewModel.getPosts(selectedType, currentUserData().userId)
        observeGetPosts()
    }

    private fun updateCommentCount(amount: Int) {
        adapter.updateCommentCount(currentItemPosition, amount)
    }

    private fun observeGetPosts() {
        viewModel.getPosts.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }

                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }

                is UiState.Success -> {
                    binding.progressBar.hide()
                    binding.srlHomeFragment.isRefreshing = false
                    postList = state.data.toMutableList()
                    isDataEmpty(postList)
                }
            }
        }
    }

    private fun isDataEmpty(data: List<Any>) {
        if (data.isEmpty()) {
            binding.rvPost.hide()
            binding.linearNoPostMessage.show()
        } else {
            binding.rvPost.show()
            binding.linearNoPostMessage.hide()
            adapter.clearData()
            adapter.updateList(postList)
            adapter.updateCurrentUser(currentUserData().userId)
        }
    }

    private fun addLike(item: String, position: Int) {
        binding.progressBar.show()
        viewModel.addLike(
            LikeData(
                likeId = "",
                likedAt = Date(),
            ), item, currentUserData().userId
        ) { state ->
            when (state) {
                is UiState.Loading -> {}

                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }

                is UiState.Success -> {
                    binding.progressBar.hide()
                    adapter.dataUpdated(position)
                    if (state.data == "Liked") adapter.updateLikeCount(position, 1)
                    else adapter.updateLikeCount(position, -1)
                }
            }
        }
    }

    private fun setMessage() {
        val c = Calendar.getInstance()
        val timeOfDay = c[Calendar.HOUR_OF_DAY]

        binding.tvMessage.text =
            when (timeOfDay) {
                in 0..11 -> "Selamat pagi"
                in 12..15 -> "Selamat siang"
                in 16..20 -> "Selamat sore"
                in 21..23 -> "Selamat malam"
                else -> "Terjasi kesalahan!"
            }

        binding.tvUserName.text = currentUserData().userName.getFirstWord().limitTextLength() + "!"

        binding.civProfilePicture.apply {
            if (currentUserData().profilePicture.isNullOrEmpty()) setImageResource(R.drawable.ic_profile_picture_default)
            else {
                Glide
                    .with(context)
                    .load(currentUserData().profilePicture)
                    .placeholder(R.drawable.ic_profile_picture_default)
                    .error(R.drawable.ic_baseline_error_24)
                    .into(binding.civProfilePicture)
            }
        }
    }

    private fun currentUserData(): UserData {
        var user = UserData()
        viewModel.getSessionData {
            if (it != null) {
                user = it
            }
        }
        return user
    }

    override fun deletePostStatus(status: Boolean?) {
        if (status == true) {
            postList.removeAt(currentItemPosition)
            adapter.updateList(postList)
            adapter.notifyItemChanged(currentItemPosition)
        }
    }

    override fun isPostBeingLiked(item: String, position: Int, callback: (Boolean) -> Unit) {
        viewModel.isPostBeingLiked(currentUserData().userId, item) {
            callback.invoke(it)
        }
    }

}