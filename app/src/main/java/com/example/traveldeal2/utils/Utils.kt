package com.example.traveldeal2.utils

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.EditText
import com.example.traveldeal2.data.entities.Travel


class Utils {
    companion object {
        fun createCall(phoneNumber: String) {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNumber")
            callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            App.instance.startActivity(callIntent)
        }

        fun sendSms(phoneNumber: String, travel: Travel) {
            val uri = Uri.parse("smsto:$phoneNumber")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("sms_body", "Here goes your message...")
            App.instance.startActivity(intent)
        }

        fun decodeKey(key: String?): String {
            return key!!.replace("\\u002e", ".").replace("\\u0024", "\$").replace("\\\\", "\\")
        }

        fun encodeKey(key: String?): String {
            return key!!.replace("\\", "\\\\").replace("\$", "\\u0024").replace(".", "\\u002e")
        }

        fun sendEmail(travel: Travel, isCompany: Boolean) {
            val companyMail =  travel.company?.filter { it.value }?.keys?.first()

            val recipient: String =  if(isCompany) decodeKey(companyMail) else travel.clientEmailAddress
            val subject = "אני מעוניין לבצע את הנסיעה.."
            val message = "וזה תוכן ההודעה שלי"
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
//            try {
//                //start email intent
//                val v = Intent.createChooser(
//                    mIntent,
//                    "Choose Email Client..."
//                )
//                v.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                App.instance.startActivity(v)
//            } catch (e: Exception) {
//                //if any thing goes wrong for example no email client application or any exception
//                //get and show exception message
//                Toast.makeText(App.instance, e.message, Toast.LENGTH_LONG).show()
//            }
        }

        fun broadcastCustomIntent(message : String) {
            val intent = Intent("MyCustomIntent")

            // add data to the Intent
            intent.putExtra("message",  message/*as CharSequence*/)
            intent.action = "com.example.traveldeal2.A_CUSTOM_INTENT"
            App.instance.sendBroadcast(intent)
        }

    }


}