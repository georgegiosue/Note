package com.nmrc.note

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private var name : String? = null
    private lateinit var appBarConfiguration: AppBarConfiguration



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        when(getFirstTimeRun()){

            0 ->  {
                    runStartActivity()
                if (savedInstanceState != null) {
                    name = savedInstanceState.getString("name")
                }
            }
            1 ->  {
                    if(name.isNullOrEmpty())
                        runStartActivity()
                    else
                        if (savedInstanceState != null) {
                            name = savedInstanceState.getString("name")
                            Toast.makeText(applicationContext,name,Toast.LENGTH_SHORT).show()
                        }
            }
            2 ->  Toast.makeText(applicationContext,"NEW VERSION!",Toast.LENGTH_SHORT).show()
        }


    }





    private fun runStartActivity(){
        val  start : Intent = Intent(applicationContext,StartApp::class.java)
        startActivity(start)
    }

    private fun getFirstTimeRun() : Int {
        var sp : SharedPreferences = getSharedPreferences("note", 0)
        var currentVersionCode : Int = BuildConfig.VERSION_CODE
        var lastVersionCode : Int = sp.getInt("FIRSTTIMERUN", -1)

        var result : Int = if (lastVersionCode == -1) 0 else if (lastVersionCode === currentVersionCode) 1 else 2
        sp.edit().putInt("FIRSTTIMERUN", currentVersionCode).apply()
        return result
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}