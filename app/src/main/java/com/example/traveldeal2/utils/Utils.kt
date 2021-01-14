package com.example.traveldeal2.utils

import android.content.Intent
import android.net.Uri
import com.example.traveldeal2.data.entities.Travel


class Utils {
    companion object {
        fun createCall(phoneNumber: String) {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNumber")
            callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            App.instance.startActivity(callIntent)
        }

        fun sendSms(phoneNumber: String, travel: Travel){
            val uri = Uri.parse("smsto:$phoneNumber")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("sms_body", "Here goes your message...")
            App.instance.startActivity(intent)

        }
    }
}