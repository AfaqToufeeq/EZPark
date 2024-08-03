package com.admin.ezpark.ui.activities

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import com.admin.ezpark.utils.Utils.mode
import com.admin.ezpark.utils.Utils.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var parentDashboardAdapter: DashboardAdapter
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
        setSupportActionBar(binding.toolbar)
        applyInsets()
        setupCoverPhoto()
        setUpRecyclerView()
        setObservers()
        initDrawer()
        setupNavController()
        setupClickListeners()
    }

    private fun setObservers() {
        viewModel.dashboardCardsType1.observe(this) { cards ->
            parentDashboardAdapter.submitList(cards.dashboardCompItems.first().second)
        }
    }

    private fun setUpRecyclerView() {
        with(binding) {
            firstCardRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                parentDashboardAdapter = DashboardAdapter { }
                adapter = parentDashboardAdapter
            }
        }
    }

    private fun applyInsets() = InsetsUtil.applyInsetsWithInitialPadding(binding.root)

    private fun setupCoverPhoto() {
        with(binding.imageView) {
            setImageResource(R.drawable.parking_cover)
            setColorFilter(
                ContextCompat.getColor(this@MainActivity, R.color.colorPrimaryTrans),
                PorterDuff.Mode.SRC_ATOP
            )
        }
    }

    private fun initDrawer() {
        drawerLayout = binding.drawerLayout
        binding.navDrawerContent.themeToggleSwitch.isChecked = sharedPreferencesManager.getBoolean(IS_DARK_MODE)
        adjustDrawerWidth()
    }

    private fun setupNavController() {
        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, drawerLayout)
        supportActionBar?.title = ""
        supportActionBar?.setHomeAsUpIndicator(R.drawable.drawer_icon)
    }


    private fun setupClickListeners() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }

            navDrawerContent.btnClose.setOnClickListener {
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            navDrawerContent.themeToggleSwitch.setOnCheckedChangeListener { _, isChecked ->
                applyTheme(isChecked)
                sharedPreferencesManager.saveBoolean(IS_DARK_MODE, isChecked)
            }
        }
    }

    private fun applyTheme(isDarkMode: Boolean) {
        mode(isDarkMode)
        // Delay to ensure UI updates are processed smoothly
        binding.navDrawerContent.themeToggleSwitch.postDelayed({
            _binding?.let {
                it.navDrawerContent.themeToggleSwitch.isChecked = isDarkMode
            }
        }, 300)
    }

    private fun adjustDrawerWidth() {
        binding.navView.post {
            val drawerWidth = (resources.displayMetrics.widthPixels / 1.5).roundToInt()
            binding.navView.layoutParams.width = drawerWidth
            binding.navView.requestLayout()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.navDrawerContent.themeToggleSwitch.removeCallbacks(null)
        _binding = null
    }
}
