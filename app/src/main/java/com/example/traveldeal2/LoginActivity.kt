package com.example.traveldeal2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    lateinit var etPhoneVerification: EditText
    lateinit var btVerify: Button
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var verifyEmail: String

        goToMainActivity()
//        etPhoneVerification = findViewById(R.id.et_phoneVerification)
//        btVerify = findViewById(R.id.bt_verify)
//        btVerify.setOnClickListener {
//            if (etPhoneVerification.text.toString() != "") {
//                verifyEmail = etPhoneVerification.text.toString()
//                verifyByEmail(verifyEmail)
//            }
//        }


//        // Check if user is signed in (non-null) and update UI accordingly.
//        if (FirebaseAuth.getInstance().currentUser != null)
//            goToMainActivity()
//        verifyByPhone(verifyPhone)
    }

//    private fun verifyByEmail(email: String) {
//        val actionCodeSettings = actionCodeSettings {
//            // URL you want to redirect back to. The domain (www.example.com) for this
//            // URL must be whitelisted in the Firebase Console.
//            url = "https://www.traveldeal2.com/?email=' + firebase.auth().currentUser.email"
//            // This must be true
//            handleCodeInApp = true
//            iosBundleId = "com.example.ios"
//            setAndroidPackageName(
//                "com.example.traveldeal2",
//                true, /* installIfNotAvailable */
//                "12" /* minimumVersion */
//            )
//        }
//
//        Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Log.d(TAG, "Email sent.")
//                }
//            }
//
//        val auth = Firebase.auth
//        val intent = intent
//        val emailLink = intent.data.toString()
//
//// Confirm the link is a sign-in with email link.
//        if (auth.isSignInWithEmailLink(emailLink)) {
//            // Retrieve this from wherever you stored it
//            //val email = "someemail@domain.com"
//
//            // The client SDK will parse the code from the link for you.
//            auth.signInWithEmailLink(email, emailLink)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Log.d(TAG, "Successfully signed in with email link!")
//                        val result = task.result
//                        goToMainActivity()
//                        // You can access the new user via result.getUser()
//                        // Additional user info profile *not* available via:
//                        // result.getAdditionalUserInfo().getProfile() == null
//                        // You can check if the user is new or existing:
//                        // result.getAdditionalUserInfo().isNewUser()
//                    } else {
//                        Log.e(TAG, "Error signing in with email link", task.exception)
//                    }
//                }
//        }
//    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }
}

