package com.example.bandungmentalhealthv10.data.repository

import android.net.Uri
import com.example.bandungmentalhealthv10.data.model.UserData
import com.example.bandungmentalhealthv10.utils.UiState

interface EditProfileRepository {
    suspend fun uploadProfilePicture(fileUri: Uri, onResult: (UiState<Uri>) -> Unit)
    fun deleteProfilePicture(imageUrl: String, result: (UiState<String>) -> Unit)
    fun updateUserProfile(
        newUserData: UserData,
        isPhotoOrNameChange: Boolean,
        result: (UiState<String>) -> Unit
    )

    fun getSessionData(result: (UserData?) -> Unit)
}