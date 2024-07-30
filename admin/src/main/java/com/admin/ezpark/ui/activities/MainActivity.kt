package com.admin.ezpark.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.admin.ezpark.R
import com.admin.ezpark.data.local.sharedpreference.SharedPreferencesManager
import com.admin.ezpark.databinding.ActivityMainBinding
import com.admin.ezpark.utils.IS_DARK_MODE
import com.admin.ezpark.utils.InsetsUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var drawerLayout: DrawerLayout
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        InsetsUtil.applyInsetsWithInitialPadding(binding.root)

        initValues()
        setupNavController()
        setUpClicks()
        adjustDrawerWidth()
    }

    private fun initValues() {
        drawerLayout = binding.drawerLayout
        setSupportActionBar(binding.toolbar)
        val isDarkMode = sharedPreferencesManager.getBoolean(IS_DARK_MODE)
        binding.navDrawerContent.themeToggleSwitch.isChecked = isDarkMode
    }

    private fun setUpClicks() {
        binding.toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.navDrawerContent.btnClose.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.navDrawerContent.themeToggleSwitch.setOnCheckedChangeListener { _, isChecked ->
            applyTheme(isChecked)
            sharedPreferencesManager.saveBoolean(IS_DARK_MODE, isChecked)
        }
    }

    private fun applyTheme(isDarkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
        // Delay to ensure UI updates are processed smoothly
        binding.navDrawerContent.themeToggleSwitch.postDelayed({
            binding.navDrawerContent.themeToggleSwitch.isChecked = isDarkMode
        }, 200)
    }

    private fun setupNavController() {
        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, drawerLayout)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun adjustDrawerWidth() {
        binding.navView.post {
            val drawerWidth = resources.displayMetrics.widthPixels / 1.5
            binding.navView.layoutParams.width = drawerWidth.roundToInt()
            binding.navView.requestLayout()
        }
    }

}