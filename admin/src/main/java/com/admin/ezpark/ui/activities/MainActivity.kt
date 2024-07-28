package com.admin.ezpark.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.admin.ezpark.databinding.ActivityMainBinding
import com.admin.ezpark.utils.InsetsUtil

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        InsetsUtil.applyInsetsWithInitialPadding(binding.root)
        splashScreen.setKeepOnScreenCondition { false }

    }
}