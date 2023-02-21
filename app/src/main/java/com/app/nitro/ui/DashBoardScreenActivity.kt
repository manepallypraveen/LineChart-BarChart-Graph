package com.app.nitro.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.app.nitro.R
import com.app.nitro.databinding.ActivityDashboardBinding
import com.app.nitro.util.onToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class DashBoardScreenActivity : AppCompatActivity() {

    lateinit var binding: ActivityDashboardBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_BottomNavWithSideNav)
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews(binding)
        observeNavElements(binding, navHostFragment.navController)



    }

    private fun refreshCurrentFragment() {
        val id = navController.currentDestination?.id
        navController.popBackStack(id!!, true)
        navController.navigate(id)
    }

    private fun initViews(binding: ActivityDashboardBinding) {
        navController = findNavController(R.id.nav_host_fragment)
        setSupportActionBar(binding.toolbarLayout.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
            ?: return

        with(navHostFragment.navController) {
            appBarConfiguration = AppBarConfiguration(graph)
            setupActionBarWithNavController(this, appBarConfiguration)
        }

    }

    private fun observeNavElements(
        binding: ActivityDashboardBinding,
        navController: NavController
    ) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                R.id.navigation_graph -> {
                    setUpToolbarBasedOnFragment(true, true)
                    showBothNavigation()
                    showToolbarNavigation()
                }
                R.id.navigation_linegraph -> {
                    setUpToolbarBasedOnFragment(true, true)
                    showBothNavigation()
                    showToolbarNavigation()
                }
                else -> {
                    showBothNavigation()
                    supportActionBar!!.setDisplayShowTitleEnabled(true)
                }
            }
        }

        binding.bottomNavView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_graph,
                R.id.navigation_linegraph
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setUpToolbarBasedOnFragment( // Based Medigin
        showInfoIcon: Boolean,
        showNotificationIcon: Boolean
    ) {
        binding.toolbarLayout.showNotificationIcon = showNotificationIcon
    }

    private fun showToolbarNavigation() {
        binding.toolbarLayout.toolbar.visibility = View.VISIBLE
    }

    private fun hideBothNavigation() {
        binding.bottomNavView.visibility = View.GONE
        binding.toolbarLayout.toolbar.visibility = View.GONE
    }

    private fun showBothNavigation() {
        binding.toolbarLayout.toolbar.visibility = View.VISIBLE
        binding.bottomNavView.visibility = View.VISIBLE
    }

    private fun clickOnNotification() {
        onToast("Clicked On Notification", applicationContext)
    }


    private fun setupNavControl() {
        binding.bottomNavView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {//   navController.navigateUp()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        if (navController.currentDestination?.id == R.id.navigation_graph) {
            super.onBackPressed()
            finish()
        } else if (navController.currentDestination?.id == R.id.navigation_linegraph) {
            super.onBackPressed()
            finish()
        } else {
            super.onBackPressed()
            finish()
        }
    }

    private fun logout() {
        MaterialAlertDialogBuilder(
            this,
            R.style.Theme_BottomNavWithSideNav_Dialog_Alert
        ).setTitle(this.resources.getString(R.string.logout))
            .setMessage(this.resources.getString(R.string.would_you_like_to_logout))
            .setPositiveButton(
                this.resources.getString(R.string.logout_ok)
            )
            { dialog, which ->
                finish()
            }.setNegativeButton(
                this.resources.getString(R.string.cancel)
            ) { dialog, which -> dialog.cancel() }.show()
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navHostFragment.navController) ||
                super.onOptionsItemSelected(item)
    }

    fun hanldeSideNavigationClicks(from: String) {
        if (from.equals(resources.getString(R.string.logout))) {
            logout()
        }
    }


}