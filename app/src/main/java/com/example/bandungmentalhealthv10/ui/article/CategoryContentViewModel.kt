package com.example.bandungmentalhealthv10.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bandungmentalhealthv10.data.model.CategoryContentData
import com.example.bandungmentalhealthv10.data.repository.CategoryContentRepository
import com.example.bandungmentalhealthv10.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryContentViewModel @Inject constructor(private val repository: CategoryContentRepository) :
    ViewModel() {

    private val _getContents = MutableLiveData<UiState<List<CategoryContentData>>>()
    val getContents: LiveData<UiState<List<CategoryContentData>>>
        get() = _getContents

    fun getContents(category: String, type: String) {
        _getContents.value = UiState.Loading
        repository.getCategoryContents(category, type) {
            _getContents.value = it
        }
    }

}