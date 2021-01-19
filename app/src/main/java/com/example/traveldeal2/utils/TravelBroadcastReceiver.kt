package com.example.traveldeal2.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.traveldeal2.utils.Utils.Companion.broadcastCustomIntent


class TravelBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        // Extract data included in the Intent
        val intentData = intent?.getCharSequenceExtra("message")
        broadcastCustomIntent(intentData)
        Toast.makeText(context,"$intentData",Toast.LENGTH_LONG).show()
    }
}