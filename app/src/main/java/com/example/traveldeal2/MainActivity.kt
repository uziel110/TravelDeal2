package com.example.traveldeal2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.traveldeal2.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (FirebaseAuth.getInstance().currentUser != null)
            return
        startSignInIntent()

//        val handler = Handler()
//        handler.postDelayed(Runnable {
//            val intent = Intent(this, AddTravelActivity::class.java)
//            this.startActivity(intent)
//            this.finish()
//        }, 500)
    }

    private fun startSignInIntent() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
        )
        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                //.setLogo(R.drawable.klipartz)
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
                    Toast.makeText(this, user.email, Toast.LENGTH_LONG).show()
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

    /** Called when the user taps the Send button */
    fun startTravelButton(view: View) {
        val intent = Intent(this, NavigationActivity::class.java)
        this.startActivity(intent)
    }
//
//    fun btMyTravels(view: View) {
//        val intent = Intent(this, AllTravelsActivity::class.java)
//        this.startActivity(intent)
//    }

    fun signOutButton(view: View) {
        FirebaseAuth.getInstance().signOut()
        startSignInIntent()
    }
}