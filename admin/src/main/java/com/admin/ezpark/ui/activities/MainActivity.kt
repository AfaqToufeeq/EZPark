package com.admin.ezpark.ui.activities

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.admin.ezpark.R
import com.admin.ezpark.data.local.sharedpreference.SharedPreferencesManager
import com.admin.ezpark.databinding.ActivityMainBinding
import com.admin.ezpark.ui.adapters.DashboardAdapter
import com.admin.ezpark.ui.viewmodels.DashboardViewModel
import com.admin.ezpark.utils.IS_DARK_MODE
import com.admin.ezpark.utils.InsetsUtil
import com.admin.ezpark.utils.Utils
import com.admin.ezpark.utils.Utils.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var dashboardAdapter: DashboardAdapter
    private val viewModel: DashboardViewModel by viewModels()

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setStatusBarColor(this, R.color.home_status_bar)
        setContentView(binding.root)

        setUpViews()
    }

    private fun setUpViews() {

        applyInsets()
        setupCoverPhoto()
        setUpRecyclerView()
        setObservers()
        initToolbar()
        initDrawer()
        setupNavController()
        setupClickListeners()
    }

    private fun setObservers() {
        viewModel.dashboardCardsType1.observe(this) { cards ->
            dashboardAdapter.submitList(cards)
        }
    }

    private fun setUpRecyclerView() {
        with(binding) {
            firstCardRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                dashboardAdapter = DashboardAdapter { dashboardCard ->
//                    navigationHandling(dashboardCard)
                }
                adapter = dashboardAdapter
            }
        }
    }

    private fun applyInsets() {
        InsetsUtil.applyInsetsWithInitialPadding(binding.root)
    }

    private fun setupCoverPhoto() {
        with(binding.imageView) {
            setImageResource(R.drawable.parking_cover)
            setColorFilter(
                ContextCompat.getColor(this@MainActivity, R.color.colorPrimaryTrans),
                PorterDuff.Mode.SRC_ATOP
            )
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        binding.toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.drawer_icon)
    }

    private fun initDrawer() {
        drawerLayout = binding.drawerLayout
        binding.navDrawerContent.themeToggleSwitch.isChecked = sharedPreferencesManager.getBoolean(IS_DARK_MODE)
        adjustDrawerWidth()
    }

    private fun setupNavController() {
        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, drawerLayout)
        supportActionBar?.title = "" // Remove default title
        binding.toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.drawer_icon)
    }

    private fun setupClickListeners() {
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

    private fun adjustDrawerWidth() {
        binding.navView.post {
            val drawerWidth = (resources.displayMetrics.widthPixels / 1.5).roundToInt()
            binding.navView.layoutParams.width = drawerWidth
            binding.navView.requestLayout()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
