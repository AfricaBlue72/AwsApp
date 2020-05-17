package com.africablue.awsapp


import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.africablue.awsapp.authproviders.AuthStatus
import com.africablue.awsapp.authproviders.AuthInjectorUtils
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel: MainActivityViewModel by viewModels{
        AuthInjectorUtils.provideMainActivityViewModelFactory()
    }
    private var userState = AuthStatus.UNKNOWN


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setNavMenu(navView)
        setDrawerListener(drawerLayout, navView)

        viewModel.userName.observe(this, Observer{
            val header = navView.getHeaderView(0)
            val headerText = header.findViewById<TextView>(R.id.textViewUser)
            headerText.text = it
        })

        viewModel.currentUserState.observe(this, Observer {
            userState = it
        })
    }

    private fun setDrawerListener(
        drawerLayout: DrawerLayout,
        navView: NavigationView
    ) {
        drawerLayout.addDrawerListener(object : SimpleDrawerListener() {
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

        val toggleButton: ToggleButton = header.findViewById(R.id.toggleButton)
        toggleButton.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                navView.menu.clear()
                navView.inflateMenu(R.menu.activity_auth_drawer);

                if(userState == AuthStatus.SIGNED_IN) {
                    navView.menu.findItem(R.id.nav_sign_in).setVisible(false)
                    navView.menu.findItem(R.id.nav_sign_out).setVisible(true)
                    navView.menu.findItem(R.id.nav_change_password).setVisible(true)
                    navView.menu.findItem(R.id.nav_forgot_password).setVisible(false)
                    navView.menu.findItem(R.id.nav_view_tokens).setVisible(true)
                }
                else
                {
                    navView.menu.findItem(R.id.nav_sign_in).setVisible(true)
                    navView.menu.findItem(R.id.nav_sign_out).setVisible(false)
                    navView.menu.findItem(R.id.nav_change_password).setVisible(false)
                    navView.menu.findItem(R.id.nav_forgot_password).setVisible(true)
                    navView.menu.findItem(R.id.nav_view_tokens).setVisible(false)
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

