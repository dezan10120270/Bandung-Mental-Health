package com.example.bandungmentalhealthv10.data.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

@kotlinx.parcelize.Parcelize
data class LikeData(
    @DocumentId
    val likeId: String = "",
    @ServerTimestamp
    val likedAt: Date = Date(),
) : Parcelable