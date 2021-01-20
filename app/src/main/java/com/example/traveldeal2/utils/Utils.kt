package com.example.traveldeal2.utils

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.traveldeal2.R
import com.example.traveldeal2.data.entities.Travel

class Utils {
    companion object {
        fun createCall(phoneNumber: String) {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNumber")
            callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            App.instance.startActivity(callIntent)
        }

        fun sendSms(travel: Travel) {
            val uri = Uri.parse("smsto:${travel.clientPhone}")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(
                "sms_body", """שלום ${travel.clientName}
אני מעוניין לבצע את הנסיעה מ ${travel.departureAddress} 
ל ${travel.destinationAddress} 
בתאריך ${travel.departureDate}
 אשמח לתת שירות
 תודה"""
            )
            App.instance.startActivity(intent)
        }

        fun decodeKey(key: String?): String {
            return key!!.replace("\\u002e", ".").replace("\\u0024", "\$").replace("\\\\", "\\")
        }

        fun encodeKey(key: String?): String {
            return key!!.replace("\\", "\\\\").replace("\$", "\\u0024").replace(".", "\\u002e")
        }

        fun sendEmail(travel: Travel, mailToCompany: Boolean) {
            var companyMail: String? = null
            if (mailToCompany)
                companyMail = travel.company.filter { it.value }.keys.first()
            val recipient: String =
                if (mailToCompany) decodeKey(companyMail) else travel.clientEmailAddress
            val subject = if (mailToCompany) "Travel Deal - נסיעה הסתיימה" else "הצעת מחיר להסעה"
            val message =
                if (mailToCompany)
                    """שלום
                        |הנסיעה מ${travel.departureAddress}
                        |ל${travel.destinationAddress}
                        |הסתיימה.
                        |נא להעביר תשלום לאתר, תודה.
                    """.trimMargin()
                else
                    """שלום ${travel.clientName}
אני מעוניין לבצע את הנסיעה מ ${travel.departureAddress} 
ל ${travel.destinationAddress} 
בתאריך ${travel.departureDate}
 אשמח לתת שירות
 תודה"""

            /*ACTION_SEND action to launch an email client installed on your Android device.*/
            val mIntent = Intent(Intent.ACTION_SEND)
            /*To send an email you need to specify mailto: as URI using setData() method
            and data type will be to text/plain using setType() method*/
            mIntent.data = Uri.parse("mailto:")
            mIntent.type = "text/plain"
            mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            // put recipient email in intent
            /* recipient is put as array because you may wanna send email to multiple emails
               so enter comma(,) separated emails, it will be stored in array*/
            mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            //put the Subject in the intent
            mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            //put the message in the intent
            mIntent.putExtra(Intent.EXTRA_TEXT, message)
            App.instance.startActivity(mIntent)
        }

        fun broadcastCustomIntent() {
            // Create an explicit intent for an Activity in your app
            val intent = Intent("com.example.traveldeal2.NOTIFICATION_INTENT").apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(App.instance, 0, intent, 0)
            val builder = NotificationCompat.Builder(App.instance, App.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_call)
                .setContentTitle("TravelDeal2")
                .setContentText("התקבלה נסיעה חדשה")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(App.instance)) {
                // notificationId is a unique int for each notification that you must define
                notify(1, builder.build())
            }
        }
    }
}