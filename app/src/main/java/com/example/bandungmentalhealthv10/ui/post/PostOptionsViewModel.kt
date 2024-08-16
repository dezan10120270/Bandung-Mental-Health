package com.example.bandungmentalhealthv10.ui.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bandungmentalhealthv10.data.model.PostData
import com.example.bandungmentalhealthv10.data.model.ReportData
import com.example.bandungmentalhealthv10.data.model.UserData
import com.example.bandungmentalhealthv10.data.repository.OptionsRepository
import com.example.bandungmentalhealthv10.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostOptionsViewModel @Inject constructor(private val repository: OptionsRepository) :
    ViewModel() {

    private val _deletePost = MutableLiveData<UiState<String>>()
    val deletePost: LiveData<UiState<String>>
        get() = _deletePost

    fun deletePost(post: PostData) {
        _deletePost.value = UiState.Loading
        repository.deletePost(post) {
            _deletePost.value = it
        }
    }

    fun addReportData(report: ReportData, result: (UiState<Boolean>) -> Unit) {
        repository.addReportData(report) {
            result.invoke(it)
        }
    }

    fun getSessionData(result: (UserData?) -> Unit) {
        repository.getSessionData(result)
    }

}