package com.example.bandungmentalhealthv10.ui.createpost

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bandungmentalhealthv10.data.model.DiaryData
import com.example.bandungmentalhealthv10.data.model.PostData
import com.example.bandungmentalhealthv10.data.model.UserData
import com.example.bandungmentalhealthv10.data.repository.PostRepository
import com.example.bandungmentalhealthv10.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(private val repository: PostRepository) :
    ViewModel() {

    private val _addPost = MutableLiveData<UiState<String>>()
    val addPost: LiveData<UiState<String>>
        get() = _addPost

    private val _addDiary = MutableLiveData<UiState<String>>()
    val addDiary: LiveData<UiState<String>>
        get() = _addDiary

    fun addPost(post: PostData) {
        _addPost.value = UiState.Loading
        repository.addPost(post) {
            _addPost.value = it
        }
    }

    fun addDiary(diary: DiaryData) {
        _addDiary.value = UiState.Loading
        repository.addDiary(diary) {
            _addDiary.value = it
        }
    }

    fun getSessionData(result: (UserData?) -> Unit) {
        repository.getSessionData(result)
    }

    fun onUploadPostImage(fileUri: Uri, onResult: (UiState<Uri>) -> Unit) {
        onResult.invoke(UiState.Loading)
        viewModelScope.launch {
            repository.uploadPostImage(fileUri, onResult)
        }
    }

    fun getProfanityCheck(caption: String, result: (UiState<String>) -> Unit) {
        viewModelScope.launch {
            repository.getProfanityCheck(caption, result)
        }
    }

}