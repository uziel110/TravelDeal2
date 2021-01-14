package com.example.traveldeal2.utils

import android.content.Intent
import android.net.Uri

class Utils {
    companion object {
        fun createCall(phoneNumber: String) {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNumber")
            callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            App.instance.startActivity(callIntent)
        }
    }
}