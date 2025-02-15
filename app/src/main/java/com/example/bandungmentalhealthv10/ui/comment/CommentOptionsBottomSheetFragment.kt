package com.example.bandungmentalhealthv10.ui.comment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.data.model.CommentData
import com.example.bandungmentalhealthv10.data.model.PostData
import com.example.bandungmentalhealthv10.data.model.ReportData
import com.example.bandungmentalhealthv10.data.model.UserData
import com.example.bandungmentalhealthv10.databinding.DialogBottomOptionsBinding
import com.example.bandungmentalhealthv10.databinding.DialogBottomReportOptionsBinding
import com.example.bandungmentalhealthv10.databinding.DialogBottomReportSuccessfulBinding
import com.example.bandungmentalhealthv10.ui.post.ReportOptionsAdapter
import com.example.bandungmentalhealthv10.ui.profile.ProfileViewModel
import com.example.bandungmentalhealthv10.ui.profile.VisitedProfileActivity
import com.example.bandungmentalhealthv10.utils.UiState
import com.example.bandungmentalhealthv10.utils.getFirstWord
import com.example.bandungmentalhealthv10.utils.hide
import com.example.bandungmentalhealthv10.utils.limitTextLength
import com.example.bandungmentalhealthv10.utils.show
import com.example.bandungmentalhealthv10.utils.toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class CommentOptionsBottomSheetFragment(private val deleteStatusListener: CommentOptionsStatusListener) :
    BottomSheetDialogFragment() {

    private var _binding: DialogBottomOptionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var objectComment: CommentData
    private lateinit var objectPost: PostData
    private var commentPosition: Int? = null
    private var isUserBeingFollowed = false
    private val viewModel: CommentOptionsViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var reportOptionsAdapter: ReportOptionsAdapter
    private lateinit var selectedReport: String
    private val reportOptionsData: List<Map<String, String>>
        get() {
            val titleData = resources.getStringArray(R.array.title_report_options)
            val descData = resources.getStringArray(R.array.desc_report_options)

            val lists = mutableListOf<Map<String, String>>()
            for (i in titleData.indices) {
                lists.add(mapOf("title" to titleData[i], "description" to descData[i]))
            }
            return lists
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        objectComment = arguments?.getParcelable("OBJECT_COMMENT")!!
        commentPosition = arguments?.getInt("COMMENT_POSITION")!!
        objectPost = arguments?.getParcelable("OBJECT_POST")!!
        _binding = DialogBottomOptionsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOptionsVisibility()

        isUserBeingFollowed()

        binding.tvDelete.setOnClickListener {
            showDeleteConfirmation()
        }

        binding.tvFollow.setOnClickListener {
            if (isUserBeingFollowed) {
                binding.tvFollow.isClickable = false
                removeFollowData()
            } else {
                binding.tvFollow.isClickable = false
                addFollowData()
            }
        }

        binding.tvSeeAccount.setOnClickListener {
            dismiss()
            val intent = Intent(requireContext(), VisitedProfileActivity::class.java)
            intent.putExtra("USER_ID", objectComment.commentedBy)
            startActivity(intent)
        }

        binding.tvReport.setOnClickListener {
            showReportCommentOptions()
        }

    }

    private fun showReportCommentOptions() {
        val reportOptionsBinding = DialogBottomReportOptionsBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(reportOptionsBinding.root)
        dialog.show()

        reportOptionsBinding.btReport.isEnabled = false
        reportOptionsAdapter =
            ReportOptionsAdapter(
                reportOptionsList = reportOptionsData,
                onItemClicked = { item ->
                    selectedReport = item
                    reportOptionsBinding.btReport.isEnabled = true
                }
            )

        reportOptionsBinding.rvReportOptions.layoutManager = LinearLayoutManager(requireContext())
        reportOptionsBinding.rvReportOptions.setHasFixedSize(true)
        reportOptionsBinding.rvReportOptions.isNestedScrollingEnabled = false
        reportOptionsBinding.rvReportOptions.adapter = reportOptionsAdapter

        reportOptionsBinding.btReport.setOnClickListener {
            reportOptionsBinding.tvReport.hide()
            reportOptionsBinding.progressBar.show()
            reportOptionsBinding.btReport.isClickable =
                false

            viewModel.addReportData(
                ReportData(
                    reportId = "",
                    reportedAt = Date(),
                    reportedBy = currentUserData().userId,
                    reportedType = "comment",
                    reportedId = objectComment.commentId,
                    reason = selectedReport,
                    status = "waiting"
                )
            ) { state ->

                when (state) {
                    is UiState.Loading -> {}

                    is UiState.Failure -> {
                        reportOptionsBinding.tvReport.show()
                        reportOptionsBinding.progressBar.hide()
                        toast(state.error)
                        reportOptionsBinding.btReport.isClickable = true
                    }

                    is UiState.Success -> {
                        dismiss()
                        dialog.dismiss()
                        if (state.data) {
                            toast("Anda sudah melaporkan komentar ini")
                        } else showReportSuccessful()
                    }
                }
            }
        }
    }

    private fun showReportSuccessful() {
        val reportSuccessfulBinding = DialogBottomReportSuccessfulBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(reportSuccessfulBinding.root)
        dialog.show()

        reportSuccessfulBinding.btDone.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun removeFollowData() {
        binding.progressBar.show()
        profileViewModel.removeFollowData(
            currentUserData().userId,
            objectComment.commentedBy
        ) { state ->
            when (state) {
                is UiState.Loading -> {}

                is UiState.Failure -> {
                    binding.progressBar.hide()
                    binding.tvFollow.isClickable = true
                    toast(state.error)
                }

                is UiState.Success -> {
                    binding.progressBar.hide()
                    binding.tvFollow.isClickable = true
                    profileViewModel.getProfileCountData(objectComment.commentedBy, "Public")
                    isUserBeingFollowed()
                }
            }
        }
    }

    private fun addFollowData() {
        binding.progressBar.show()
        profileViewModel.addFollowData(
            currentUserData().userId,
            objectComment.commentedBy
        ) { state ->
            when (state) {
                is UiState.Loading -> {}

                is UiState.Failure -> {
                    binding.progressBar.hide()
                    binding.tvFollow.isClickable = true
                    toast(state.error)
                }

                is UiState.Success -> {
                    binding.progressBar.hide()
                    binding.tvFollow.isClickable = true
                    profileViewModel.getProfileCountData(
                        objectComment.commentedBy,
                        "Public"
                    )
                    isUserBeingFollowed()
                }
            }
        }
    }

    private fun isUserBeingFollowed() {
        profileViewModel.isUserBeingFollowed(currentUserData().userId, objectComment.commentedBy) {
            if (it) {
                isUserBeingFollowed = true
                setFollowTextView(true)
            } else {
                isUserBeingFollowed = false
                setFollowTextView(false)
            }
        }
    }

    private fun setFollowTextView(isFollowed: Boolean) {
        if (isFollowed) {
            binding.tvFollow.text =
                "Berhenti Mengikuti " + objectComment.userName.getFirstWord().limitTextLength()
            binding.tvFollow.setCompoundDrawablesWithIntrinsicBounds(
                ResourcesCompat.getDrawable(resources, R.drawable.ic_unfollow, null),
                null,
                null,
                null
            )
        } else {
            binding.tvFollow.text =
                "Ikuti " + objectComment.userName.getFirstWord().limitTextLength()
            binding.tvFollow.setCompoundDrawablesWithIntrinsicBounds(
                ResourcesCompat.getDrawable(resources, R.drawable.ic_follow, null),
                null,
                null,
                null
            )
        }
    }

    private fun showDeleteConfirmation() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_delete_confirmation)

        val tvMessageTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        val tvMessageDescription = dialog.findViewById<TextView>(R.id.tvDescription)
        val tvCancel = dialog.findViewById<TextView>(R.id.tvCancel)
        val tvDelete = dialog.findViewById<TextView>(R.id.tvDelete)
        val progressBar = dialog.findViewById<ProgressBar>(R.id.progressBar)

        tvMessageTitle.text = getString(R.string.delete_comment_title)
        tvMessageDescription.text = getString(R.string.delete_comment_description)

        tvCancel.setOnClickListener {
            dialog.dismiss()
        }
        tvDelete.setOnClickListener {
            tvDelete.isClickable = false
            viewModel.deleteComment(objectComment)

            viewModel.deleteComment.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is UiState.Loading -> {
                        progressBar.show()
                    }

                    is UiState.Failure -> {
                        progressBar.hide()
                        toast(state.error)
                        dialog.dismiss()
                        dismiss()
                    }

                    is UiState.Success -> {
                        progressBar.hide()
                        toast(state.data)
                        deleteStatusListener.deleteStatus(true, commentPosition)
                        dialog.dismiss()
                        dismiss()
                    }
                }
            }
        }

        dialog.show()

    }

    private fun setOptionsVisibility() {
        binding.tvDelete.text = "Hapus Komentar"
        binding.tvReport.text = "Laporkan Komentar"

        if (objectComment.commentedBy == currentUserData().userId) {
            binding.tvFollow.hide()
            binding.tvSeeAccount.hide()
            binding.tvReport.hide()
            binding.tvDelete.show()
        } else if (objectPost.type == "Anonymous" && objectPost.createdBy == objectComment.commentedBy) {
            binding.tvFollow.hide()
            binding.tvSeeAccount.hide()
            binding.tvReport.show()
            binding.tvDelete.hide()
        } else {
            binding.tvFollow.show()
            binding.tvSeeAccount.show()
            binding.tvReport.show()
            binding.tvDelete.hide()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface CommentOptionsStatusListener {
        fun deleteStatus(status: Boolean?, position: Int?)
    }

}