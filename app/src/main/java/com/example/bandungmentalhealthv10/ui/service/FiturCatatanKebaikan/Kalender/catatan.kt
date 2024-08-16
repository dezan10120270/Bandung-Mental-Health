package com.example.bandungmentalhealthv10.ui.service.FiturCatatanKebaikan.Kalender

data class Catatan(
    val tanggal: String? = null,
    val kegiatan: String? = null
) {
    // Konstruktor tanpa argumen diperlukan oleh Firebase
    @Suppress("unused")
    constructor() : this("", "")
}

