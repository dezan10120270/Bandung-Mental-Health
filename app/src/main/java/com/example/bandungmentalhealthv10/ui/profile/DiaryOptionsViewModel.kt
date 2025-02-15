package com.example.bandungmentalhealthv10.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bandungmentalhealthv10.data.model.DiaryData
import com.example.bandungmentalhealthv10.data.model.UserData
import com.example.bandungmentalhealthv10.data.repository.OptionsRepository
import com.example.bandungmentalhealthv10.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiaryOptionsViewModel @Inject constructor(private val repository: OptionsRepository) :
    ViewModel() {

    private val _deleteDiary = MutableLiveData<UiState<String>>()
    val deleteDiary: LiveData<UiState<String>>
        get() = _deleteDiary

    fun deleteDiary(diary: DiaryData) {
        _deleteDiary.value = UiState.Loading
        repository.deleteDiary(diary) {
            _deleteDiary.value = it
        }
    }

    fun getSessionData(result: (UserData?) -> Unit) {
        repository.getSessionData(result)
    }

}