package com.example.traveldeal2.utils

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MyService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sendBroadcast(intent)
        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        // We don't provide binding, so return null
        return null
    }
}