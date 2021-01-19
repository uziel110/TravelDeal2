package com.example.traveldeal2

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.ui.*
import com.example.traveldeal2.utils.TravelBroadcastReceiver
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 123
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView: NavigationView
    private lateinit var userMailTextView: TextView
    private lateinit var userNameTextView: TextView
    private lateinit var intentFilter: IntentFilter
    private lateinit var toolbar: Toolbar
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setViews()

        setSupportActionBar(toolbar)
        setNavigationDrawer()

        sharedPreferences = getSharedPreferences("name", MODE_PRIVATE)

        //brodCast receiver
        intentFilter = IntentFilter()
        intentFilter.addAction("com.example.traveldeal2.A_CUSTOM_INTENT")
        registerReceiver(TravelBroadcastReceiver(), intentFilter)

        if (sharedPreferences.getBoolean("user", false))
            return
        startSignInIntent()
    }

    private fun setViews() {

        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_myTravels, R.id.nav_admin, R.id.nav_searchTravels, R.id.nav_signOut
            ), drawerLayout
        )


        val headerView = navView.getHeaderView(0)
        userMailTextView =
            headerView.findViewById<View>(R.id.userMailTextView) as TextView
        userMailTextView.text = FirebaseAuth.getInstance().currentUser?.email

        userNameTextView = headerView.findViewById<View>(R.id.userNameTextView) as TextView
        userNameTextView.text = FirebaseAuth.getInstance().currentUser?.displayName

    }

    private fun setNavigationDrawer() {

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView, navController)

        navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            if (id == R.id.nav_signOut)
                signOut()
            //This is for maintaining the behavior of the Navigation view
            NavigationUI.onNavDestinationSelected(menuItem, navController)
            //This is for closing the drawer after acting on it
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        })

//        if (FirebaseAuth.getInstance().currentUser != null)
//            return
//        startSignInIntent()
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
                    Toast.makeText(this, "Welcome ${user.displayName}", Toast.LENGTH_LONG).show()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(applicationContext, "You are signed out", Toast.LENGTH_SHORT).show()
        startSignInIntent()
    }
}