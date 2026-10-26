package com.example.tenantmanagementscreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tenantmanagementscreen.databinding.ActivityMainBinding
import com.example.tenantmanagementscreen.databinding.NavHeaderBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TENANT_LIFECYCLE", "MainActivity: onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Use our Toolbar as the App Bar
        setSupportActionBar(binding.toolbar)

        // 2. Get the NavController from the NavHost
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // 3. Top-level destinations (show the drawer icon) + the drawer
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.dashboardFragment, R.id.addTenantFragment, R.id.viewTenantsFragment),
            binding.drawerLayout
        )

        // 4. Connect the App Bar and the drawer to navigation
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        // 5. Show the email sent by LoginActivity in the drawer header
        val email = intent.getStringExtra("EMAIL")
        val headerBinding = NavHeaderBinding.bind(binding.navView.getHeaderView(0))
        headerBinding.headerEmailTextView.text = email ?: "Signed in"
    }

    // The drawer icon opens the drawer; the arrow goes back
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Turn main_menu.xml into menu items in the App Bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Logout is handled here; other items match a destination ID in the nav graph
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            return true
        }
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    // LIFECYCLE LOGS
    override fun onStart() { super.onStart(); Log.d("TENANT_LIFECYCLE", "MainActivity: onStart") }
    override fun onResume() { super.onResume(); Log.d("TENANT_LIFECYCLE", "MainActivity: onResume") }
    override fun onPause() { super.onPause(); Log.d("TENANT_LIFECYCLE", "MainActivity: onPause") }
    override fun onStop() { super.onStop(); Log.d("TENANT_LIFECYCLE", "MainActivity: onStop") }
    override fun onRestart() { super.onRestart(); Log.d("TENANT_LIFECYCLE", "MainActivity: onRestart") }
    override fun onDestroy() { super.onDestroy(); Log.d("TENANT_LIFECYCLE", "MainActivity: onDestroy") }
}