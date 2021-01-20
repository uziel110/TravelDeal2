package com.example.traveldeal2.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.traveldeal2.utils.Utils.Companion.broadcastCustomIntent

class TravelBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if ("com.example.traveldeal.NewTravel" == intent?.action) {
            // Extract data included in the Intent
            broadcastCustomIntent()
            Toast.makeText(context, "נכנסה נסיעה חדשה", Toast.LENGTH_LONG).show()
        }
    }
}