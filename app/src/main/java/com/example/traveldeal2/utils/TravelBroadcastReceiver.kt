package com.example.traveldeal2.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.Global.getString
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.traveldeal2.R
import com.example.traveldeal2.utils.Utils.Companion.broadcastCustomIntent


class TravelBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        // Extract data included in the Intent
        val intentData = intent?.getCharSequenceExtra("message")
        broadcastCustomIntent(intentData)
        Toast.makeText(context,"$intentData",Toast.LENGTH_LONG).show()
    }

}