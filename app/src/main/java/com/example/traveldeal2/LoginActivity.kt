package com.example.traveldeal2

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.traveldeal2.ui.myTravels.Strings
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 123
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("name", MODE_PRIVATE)

        if (sharedPreferences.getBoolean("user", false)) {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
            return
        }
        startSignInIntent()
    }


    private fun startSignInIntent() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
            //,AuthUI.IdpConfig.PhoneBuilder().build()
        )
        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .setLogo(R.drawable.app_logo)
                .build(),
            RC_SIGN_IN
        )
        // [END auth_fui_create_intent]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    sharedPreferences.edit().putBoolean("user", true).apply()
                    sharedPreferences.edit().putString("userMail", user.email).apply()
                    Toast.makeText(this,"${Strings.get(R.string.welcome)} " + user.displayName
                        , Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    this.startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Sign in failed", Toast.LENGTH_LONG).show()
                startSignInIntent()
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
}