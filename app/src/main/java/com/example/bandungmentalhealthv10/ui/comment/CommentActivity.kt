package com.example.bandungmentalhealthv10.ui.comment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.data.model.CommentData
import com.example.bandungmentalhealthv10.data.model.PostData
import com.example.bandungmentalhealthv10.data.model.UserData
import com.example.bandungmentalhealthv10.databinding.ActivityPostCommentBinding
import com.example.bandungmentalhealthv10.ui.profile.VisitedProfileActivity
import com.example.bandungmentalhealthv10.utils.UiState
import com.example.bandungmentalhealthv10.utils.hide
import com.example.bandungmentalhealthv10.utils.hideKeyboard
import com.example.bandungmentalhealthv10.utils.limitTextLength
import com.example.bandungmentalhealthv10.utils.show
import com.example.bandungmentalhealthv10.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class CommentActivity : AppCompatActivity(),
    CommentOptionsBottomSheetFragment.CommentOptionsStatusListener {

    private lateinit var binding: ActivityPostCommentBinding
    private val viewModel: CommentViewModel by viewModels()
    private var objectPost: PostData? = null
    private var commentList: MutableList<CommentData> = arrayListOf()
    private var commentAddedAmount = 0
    private val adapter by lazy {
        CommentAdapter(
            onOptionsCommentClicked = { pos, item ->
                val commentOptionsBottomDialogFragment = CommentOptionsBottomSheetFragment(this)

                val bundle = Bundle()
                bundle.putParcelable("OBJECT_COMMENT", item)
                bundle.putInt("COMMENT_POSITION", pos)
                bundle.putParcelable("OBJECT_POST", objectPost)
                commentOptionsBottomDialogFragment.arguments = bundle

                commentOptionsBottomDialogFragment.show(
                    supportFragmentManager,
                    "CommentOptionsBottomSheetFragment"
                )
            },
            onProfileClicked = { _, item ->
                if (item == currentUserData().userId) {
                } else {
                    val intent = Intent(this, VisitedProfileActivity::class.java)
                    intent.putExtra("USER_ID", item)
                    startActivity(intent)
                }
            }
        )
    }
    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setResultData()
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        objectPost = intent.getParcelableExtra("OBJECT_POST")
        setObjectPost()

        setToolbar()

        binding.srlPostCommentActivity.setOnRefreshListener {
            viewModel.getComments(objectPost!!.postId)
        }

        binding.rvComment.adapter = adapter
        binding.rvComment.layoutManager = LinearLayoutManager(this)
        binding.rvComment.setHasFixedSize(true)
        binding.rvComment.isNestedScrollingEnabled = false

        viewModel.getComments(objectPost!!.postId)
        observeGetComments()

        binding.ivSend.setOnClickListener {
            binding.ivSend.isClickable =
                false
            hideKeyboard()
            if (inputValidation()) {
                isCommentContainProfanity()
            }
        }

        observeAddComment()
    }

    private fun isCommentContainProfanity() {
        binding.progressBar.show()
        viewModel.getProfanityCheck(binding.etComment.text.toString()) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }

                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                    binding.ivSend.isClickable = true
                }

                is UiState.Success -> {
                    binding.progressBar.hide()
                    if (state.data.toBoolean()) {
                        showProfanityDialog()
                        binding.ivSend.isClickable = true
                    } else {
                        addCommentData()
                    }
                }
            }
        }
    }

    private fun addCommentData() {
        viewModel.addComment(
            CommentData(
                commentId = "",
                commentedBy = currentUserData().userId,
                commentedAt = Date(),
                comment = binding.etComment.text.toString(),
                userName = currentUserData().userName,
                profilePicture = currentUserData().profilePicture,
                userBadge = currentUserData().badge,
                postId = objectPost!!.postId
            )
        )
    }

    private fun showProfanityDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_profanity_detected)
        dialog.show()

        dialog.findViewById<ImageView>(R.id.ivCloseDialog).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun observeGetComments() {
        viewModel.getComments.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }

                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }

                is UiState.Success -> {
                    binding.srlPostCommentActivity.isRefreshing = false
                    binding.progressBar.hide()
                    if (state.data.isNotEmpty()) {
                        binding.rvComment.show()
                        binding.linearNoCommentMessage.hide()
                        commentList = state.data.toMutableList()
                        adapter.updateList(commentList)
                        adapter.updatePostType(objectPost!!)
                        adapter.updateCurrentUser(currentUserData().userId)
                    } else {
                        binding.rvComment.hide()
                        binding.linearNoCommentMessage.show()
                    }
                }
            }
        }
    }

    private fun observeAddComment() {
        viewModel.addComment.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }

                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                    binding.ivSend.isClickable = true
                }

                is UiState.Success -> {
                    binding.progressBar.hide()
                    commentAddedAmount += 1
                    viewModel.getComments(objectPost!!.postId)
                    binding.etComment.text?.clear()
                    binding.ivSend.isClickable = true
                }
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

    private fun inputValidation(): Boolean {
        var isValid = true

        if (binding.etComment.text.toString().trim().isEmpty()) {
            isValid = false
            binding.etComment.error = "Komentar tidak boleh kosong!"
            binding.ivSend.isClickable = true
        }

        return isValid
    }

    private fun setObjectPost() {
        val sdf = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))

        binding.tvUserNamePost.apply {
            text = if (objectPost?.type == "Anonymous") {
                if (objectPost?.createdBy == currentUserData().userId) {
                    "Anonymous (You)"
                } else {
                    "Anonymous"
                }
            } else {
                objectPost?.userName?.limitTextLength()
            }
        }

        binding.tvCategoryPost.text = objectPost?.category
        binding.tvTimePost.text = sdf.format(objectPost?.createdAt!!)
        binding.tvCaptionPost.text = objectPost?.caption

        binding.ivUserBadgePost.apply {
            if (objectPost?.type == "Anonymous") {
                hide()
            } else {
                when (objectPost?.userBadge) {
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
            if (objectPost?.type == "Anonymous") {
                setImageResource(R.drawable.ic_profile_picture_anonymous)
            } else {
                if (objectPost?.profilePicture.isNullOrEmpty()) setImageResource(R.drawable.ic_profile_picture_default)
                else {
                    Glide
                        .with(context)
                        .load(objectPost?.profilePicture)
                        .placeholder(R.drawable.ic_profile_picture_default)
                        .error(R.drawable.ic_baseline_error_24)
                        .into(binding.civProfilePicturePost)
                }
            }
        }
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_grey)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setResultData() {
        if (commentAddedAmount != 0) {
            val intent = Intent()
            intent.putExtra("COMMENT_ADDED", commentAddedAmount)
            setResult(Activity.RESULT_OK, intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        setResultData()
        finish()
        return true
    }

    override fun deleteStatus(status: Boolean?, position: Int?) {
        if (status == true) {
            position?.let { commentList.removeAt(it) }
            adapter.updateList(commentList)
            position?.let { adapter.notifyItemChanged(it) }
            commentAddedAmount -= 1
        }
    }
}