package com.example.bandungmentalhealthv10.utils

import android.app.Activity
import android.content.Context
import android.content.Intent

object FunPindah {
    fun pindahScene(context: Context, tujuan: Class<out Activity>) {
        val intent = Intent(context, tujuan)
        context.startActivity(intent)
    }

    fun onBackPressed(context: Context) {
        if (context is Activity) {
            (context as Activity).onBackPressed()
        }
    }
}
