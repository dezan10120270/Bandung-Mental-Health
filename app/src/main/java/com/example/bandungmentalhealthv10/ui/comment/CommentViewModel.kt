package com.example.bandungmentalhealthv10.ui.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bandungmentalhealthv10.data.model.CommentData
import com.example.bandungmentalhealthv10.data.model.UserData
import com.example.bandungmentalhealthv10.data.repository.PostRepository
import com.example.bandungmentalhealthv10.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    private val _getComments = MutableLiveData<UiState<List<CommentData>>>()
    val getComments: LiveData<UiState<List<CommentData>>>
        get() = _getComments

    private val _addComment = MutableLiveData<UiState<String>>()
    val addComment: LiveData<UiState<String>>
        get() = _addComment

    fun getComments(postId: String) {
        _getComments.value = UiState.Loading
        repository.getComments(postId) {
            _getComments.value = it
        }
    }

    fun addComment(comment: CommentData) {
        _addComment.value = UiState.Loading
        repository.addComment(comment) {
            _addComment.value = it
        }
    }

    fun getProfanityCheck(caption: String, result: (UiState<String>) -> Unit) {
        viewModelScope.launch {
            repository.getProfanityCheck(caption, result)
        }
    }

    fun getSessionData(result: (UserData?) -> Unit) {
        repository.getSessionData(result)
    }


}