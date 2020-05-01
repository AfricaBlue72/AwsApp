package com.example.awsapp


import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.UserState
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_sign_in), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setNavMenu(navView)
        setDrawerListener(drawerLayout, navView)
    }

    private fun setDrawerListener(
        drawerLayout: DrawerLayout,
        navView: NavigationView
    ) {
        drawerLayout.addDrawerListener(object : SimpleDrawerListener() {
            override fun onDrawerOpened(drawerView: View) {
                val header = navView.getHeaderView(0)
                val headerText = header.findViewById<TextView>(R.id.textViewUser)
                val userName = AWSMobileClient.getInstance().username
                headerText.text = userName ?: getString(R.string.auth_header_user_guest)
            }

            override fun onDrawerClosed(drawerView: View) {
                val header = navView.getHeaderView(0)
                val toggleButton: ToggleButton = header.findViewById(R.id.toggleButton)
                toggleButton.isChecked = false

                navView.menu.clear()
                navView.inflateMenu(R.menu.activity_main_drawer);
            }
        })
    }

    private fun setNavMenu(navView: NavigationView) {
        val header = navView.getHeaderView(0)
        val awsClient = AWSMobileClient.getInstance()

        val toggleButton: ToggleButton = header.findViewById(R.id.toggleButton)
        toggleButton.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                navView.menu.clear()
                navView.inflateMenu(R.menu.activity_auth_drawer);

                val state = awsClient.currentUserState()

                if(awsClient.currentUserState().userState == UserState.SIGNED_IN) {
                    navView.menu.findItem(R.id.nav_sign_in).setVisible(false)
                    navView.menu.findItem(R.id.nav_sign_out).setVisible(true)
                    navView.menu.findItem(R.id.nav_change_password).setVisible(true)
                    navView.menu.findItem(R.id.nav_forgot_password).setVisible(false)
                }
                else
                {
                    navView.menu.findItem(R.id.nav_sign_in).setVisible(true)
                    navView.menu.findItem(R.id.nav_sign_out).setVisible(false)
                    navView.menu.findItem(R.id.nav_change_password).setVisible(false)
                    navView.menu.findItem(R.id.nav_forgot_password).setVisible(true)
                }

            } else {
                navView.menu.clear()
                navView.inflateMenu(R.menu.activity_main_drawer);
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




}

