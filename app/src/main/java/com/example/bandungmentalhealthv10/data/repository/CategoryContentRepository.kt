package com.example.bandungmentalhealthv10.data.repository

import com.example.bandungmentalhealthv10.data.model.*
import com.example.bandungmentalhealthv10.utils.UiState

interface CategoryContentRepository {
    fun getCategoryContents(
        category: String,
        type: String,
        result: (UiState<List<CategoryContentData>>) -> Unit
    )
}