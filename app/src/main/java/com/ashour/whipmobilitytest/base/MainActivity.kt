package com.ashour.whipmobilitytest.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.ashour.whipmobilitytest.R
import com.ashour.whipmobilitytest.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var activityBaseBinding: ActivityMainBinding
    private val viewModel: BaseViewModel by viewModels()
    lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBaseBinding = ActivityMainBinding.inflate(layoutInflater)
        activityBaseBinding.lifecycleOwner = this
        setContentView(activityBaseBinding.root)
        setSupportActionBar(activityBaseBinding.toolbar)
        init()
    }

    private fun init() {
        val navHost = supportFragmentManager.findFragmentById(R.id.main_nav_fragment) as NavHostFragment
        navController = navHost.navController
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setupActionBarWithNavController(navController)
        activityBaseBinding.viewModel = viewModel

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val label = destination.label?:""
            activityBaseBinding.toolbarTitle.text = label
        }
    }

    fun showHideProgress(show: Boolean) {
        viewModel.dataLoading.value = show
    }

}