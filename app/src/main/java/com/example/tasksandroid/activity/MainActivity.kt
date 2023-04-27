package com.example.tasksandroid.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.NavigationUI
import com.example.tasksandroid.R
import com.example.tasksandroid.databinding.ActivityMainBinding
import com.example.tasksandroid.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            startActivity(Intent(applicationContext, TaskFormActivity::class.java))
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_all_tasks, R.id.nav_next7day, R.id.nav_overdue
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener {
            if(it.itemId == R.id.logout){
                viewModel.logout()
                finish()
            }else{
                NavigationUI.onNavDestinationSelected(it, navController)
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            true
        }

        viewModel.loadUserName()
        observes()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun observes(){
        viewModel.userName.observe(this){
            val header = binding.navView.getHeaderView(0)
            header.findViewById<TextView>(R.id.user_name_text).text = it
        }
    }
}