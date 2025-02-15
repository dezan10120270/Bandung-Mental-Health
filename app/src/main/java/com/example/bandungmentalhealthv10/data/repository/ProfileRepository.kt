package com.example.bandungmentalhealthv10.data.repository

import com.example.bandungmentalhealthv10.data.model.ProfileCountData
import com.example.bandungmentalhealthv10.data.model.UserData
import com.example.bandungmentalhealthv10.utils.UiState

interface ProfileRepository {
    fun getSessionData(result: (UserData?) -> Unit)
    fun getUserData(uid: String, result: (UiState<UserData?>) -> Unit)
    fun getProfileCountData(
        uid: String,
        postType: String,
        result: (UiState<ProfileCountData>) -> Unit
    )

    fun addFollowData(currentUId: String, followedUId: String, result: (UiState<String>) -> Unit)
    fun removeFollowData(currentUId: String, followedUId: String, result: (UiState<String>) -> Unit)
    fun getUserDataList(uid: String, collection: String, result: (UiState<List<UserData>>) -> Unit)
    fun isUserBeingFollowed(currentUId: String, targetUId: String, result: (Boolean) -> Unit)
}