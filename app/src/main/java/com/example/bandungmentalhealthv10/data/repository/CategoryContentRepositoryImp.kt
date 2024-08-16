package com.example.bandungmentalhealthv10.data.repository

import com.example.bandungmentalhealthv10.data.model.CategoryContentData
import com.example.bandungmentalhealthv10.utils.FirestoreCollection
import com.example.bandungmentalhealthv10.utils.FirestoreDocumentField
import com.example.bandungmentalhealthv10.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class CategoryContentRepositoryImp(
    private val database: FirebaseFirestore
) : CategoryContentRepository {

    override fun getCategoryContents(
        category: String,
        type: String,
        result: (UiState<List<CategoryContentData>>) -> Unit
    ) {
        database.collection(FirestoreCollection.CATEGORY_CONTENT)
            .whereEqualTo(FirestoreDocumentField.CONTENT_TYPE, type)
            .whereEqualTo(FirestoreDocumentField.CONTENT_CATEGORY, category)
            .orderBy(FirestoreDocumentField.CONTENT_PUBLISHED_DATE, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(
                        UiState.Failure(it.localizedMessage)
                    )
                }

                value?.let {
                    val contents = arrayListOf<CategoryContentData>()
                    for (document in it) {
                        val content = document.toObject(CategoryContentData::class.java)
                        contents.add(content)
                    }
                    result.invoke(
                        UiState.Success(contents)
                    )
                }
            }
    }

}