package com.example.traveldeal2.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavDeepLinkBuilder
import com.example.traveldeal2.MainActivity
import com.example.traveldeal2.R
import com.example.traveldeal2.ui.company.CompanyTravelsFragment

class TravelBroadcastReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent?) {
        if ("com.example.traveldeal.NewTravel" == intent?.action) {
            // Extract data included in the Intent
//            broadcastCustomIntent()
//            Toast.makeText(context, "נכנסה נסיעה חדשה", Toast.LENGTH_LONG).show()

            val id = App.CHANNEL_ID //id of channel
            val description = "New Travel" //Description information of channel
            val importance = NotificationManager.IMPORTANCE_HIGH //The Importance of channel
            val channel = NotificationChannel(id, description, importance) //Generating channel
            // prepare intent which is triggered if the
            // notification is selected
            val intent = Intent(context, MainActivity::class.java)
            //what will be open when clicking on notification
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            val mBuilder: Notification.Builder = Notification.Builder(context, id)
            mBuilder.setSmallIcon(R.drawable.ic_bus)
                .setContentTitle("נסיעה חדשה נכנסה")
                .setContentText("נכנסה נסיעה חדשה הזדרז לתפוס אותה ראשון")
                .setContentIntent(pendingIntent)
            // Gets an instance of the NotificationManager service
            val mNotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(id)
            mNotificationManager.notify(1, mBuilder.build())
        }
    }
}