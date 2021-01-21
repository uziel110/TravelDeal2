package com.example.traveldeal2

import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.example.traveldeal2.ui.AdminDialog
import com.example.traveldeal2.utils.TravelBroadcastReceiver
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

const val ADMIN_PASSWORD: String = "123456"

class MainActivity : AppCompatActivity(), AdminDialog.AdminDialogListener {
    //    private val RC_SIGN_IN = 123
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
        registerReceiver(
            TravelBroadcastReceiver(), IntentFilter("com.example.traveldeal.NewTravel")
        )
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
            if (id == R.id.nav_admin) {// && FirebaseAuth.getInstance().uid != "RfAzMEgn6mRHBW7NTWFRDKeX06Y2") {// ari's Uid
//            if (id == R.id.nav_admin && FirebaseAuth.getInstance().uid != "JzUvUuHVsIgILDm7Xn0gcW9wS1A3") {// Uziel Uid
//                Toast.makeText(this, "אתה לא בעל האתר המורשה", Toast.LENGTH_LONG).show()
                openDialog()
            } else {
                //This is for maintaining the behavior of the Navigation view
                NavigationUI.onNavDestinationSelected(menuItem, navController)
            }
            //This is for closing the drawer after acting on it
            drawerLayout.closeDrawer(GravityCompat.START)
            true

        })
    }

    private fun openDialog() {
        val adminDialog = AdminDialog()
        adminDialog.show(supportFragmentManager, "adminDialog")
    }

    override fun checkAdminPassword(password: String) {
        if (password == ADMIN_PASSWORD)
            NavigationUI.onNavDestinationSelected(navView.checkedItem!!, navController)
        else
            Toast.makeText(this, "אתה לא בעל האתר המורשה", Toast.LENGTH_LONG).show()
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

        val intent = Intent(this, LoginActivity::class.java)
        sharedPreferences.edit().putBoolean("user", false).apply()
        sharedPreferences.edit().putString("userMail", "").apply()
        this.startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(TravelBroadcastReceiver())
    }
}