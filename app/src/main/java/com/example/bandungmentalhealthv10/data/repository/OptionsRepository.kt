package com.example.bandungmentalhealthv10.data.repository

import com.example.bandungmentalhealthv10.data.model.*
import com.example.bandungmentalhealthv10.utils.UiState

interface OptionsRepository {
    fun deletePost(post: PostData, result: (UiState<String>) -> Unit)
    fun deleteComment(comment: CommentData, result: (UiState<String>) -> Unit)
    fun deleteDiary(diary: DiaryData, result: (UiState<String>) -> Unit)
    fun addReportData(report: ReportData, result: (UiState<Boolean>) -> Unit)
    fun getSessionData(result: (UserData?) -> Unit)
}