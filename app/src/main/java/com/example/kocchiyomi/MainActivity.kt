package com.example.kocchiyomi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kocchiyomi.databinding.ActivityMainBinding
import com.example.kocchiyomi.utils.AuthUtil
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import androidx.lifecycle.lifecycleScope
import arrow.core.computations.result
import com.example.kocchiyomi.ui.auth.AuthActivity
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(androidx.navigation.fragment.R.id.nav_host_fragment_container) as NavHostFragment

        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.libraryFragment,
                R.id.browseFragment,
                R.id.historyFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.appBarMain.bottomNav.setupWithNavController(navController)
        binding.appBarMain.bottomNav.setOnItemReselectedListener {  }

        lifecycleScope.launch {
            setDrawerHeader()
        }

        setDrawerBody()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_container)
        return  navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private suspend fun setDrawerHeader() {
        val navigationView : NavigationView = findViewById(R.id.nav_view)
        val headerNav = navigationView.getHeaderView(0)
        val tvHeaderUsername = headerNav.findViewById<TextView>(R.id.tv_header_username)
        val tvHeaderEmail = headerNav.findViewById<TextView>(R.id.tv_header_email)

        // bikin versi viewmodelnya
        if (AuthUtil.firebaseAuthInstance.currentUser != null) {
            try{
                val currentUser = AuthUtil.getUserDetail()
                tvHeaderUsername.text = currentUser.userName
                tvHeaderEmail.text = currentUser.email
            } catch (e: Exception){
                Log.w("Firebase Auth Exception", e.toString())
            }

        }
    }

    private fun setDrawerBody() {
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val menuNav = navigationView.menu
        val navLogout = menuNav.findItem(R.id.nav_logout)

        navLogout.setOnMenuItemClickListener {
            AuthUtil.firebaseSignOut()
            navigateToAuth()
            true
        }
    }

    private fun navigateToAuth() {
        val intent = Intent(this@MainActivity, AuthActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

}