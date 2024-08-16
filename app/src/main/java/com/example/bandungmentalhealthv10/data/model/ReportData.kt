package com.example.bandungmentalhealthv10.data.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

@kotlinx.parcelize.Parcelize
data class ReportData(
    @DocumentId
    var reportId: String = "",
    @ServerTimestamp
    val reportedAt: Date = Date(),
    val reportedBy: String = "",
    val reportedType: String = "",
    val reportedId: String = "",
    val reason: String = "",
    val status: String = ""
) : Parcelable