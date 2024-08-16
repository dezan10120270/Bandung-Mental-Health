package com.example.bandungmentalhealthv10.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.data.model.ReportData
import com.example.bandungmentalhealthv10.data.model.UserData
import com.example.bandungmentalhealthv10.databinding.DialogBottomOptionsBinding
import com.example.bandungmentalhealthv10.databinding.DialogBottomReportOptionsBinding
import com.example.bandungmentalhealthv10.databinding.DialogBottomReportSuccessfulBinding
import com.example.bandungmentalhealthv10.ui.post.ReportOptionsAdapter
import com.example.bandungmentalhealthv10.utils.UiState
import com.example.bandungmentalhealthv10.utils.hide
import com.example.bandungmentalhealthv10.utils.show
import com.example.bandungmentalhealthv10.utils.toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class VisitedProfileOptionsBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: DialogBottomOptionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var visitedUserId: String
    private val viewModel: VisitedProfileOptionsViewModel by viewModels()
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
        visitedUserId = arguments?.getString("VISITED_UID")!!
        _binding = DialogBottomOptionsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOptionsVisibility()

        binding.tvReport.setOnClickListener {
            showReportUserOptions()
        }

    }

    private fun showReportUserOptions() {
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
                    reportedType = "user",
                    reportedId = visitedUserId,
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
                            toast("Anda sudah melaporkan pengguna ini")
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

    private fun setOptionsVisibility() {
        binding.tvReport.text = "Laporkan Pengguna"

        binding.tvReport.show()
        binding.tvDelete.hide()
        binding.tvFollow.hide()
        binding.tvSeeAccount.hide()
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

}