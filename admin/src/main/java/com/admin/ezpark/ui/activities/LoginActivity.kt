package com.admin.ezpark.ui.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.admin.ezpark.data.local.sharedpreference.SharedPreferencesManager
import com.admin.ezpark.databinding.ActivityLoginBinding
import com.admin.ezpark.ui.viewmodels.AdminViewModel
import com.admin.ezpark.utils.IS_LOGIN
import com.admin.ezpark.utils.InsetsUtil
import com.admin.ezpark.utils.Utils.navigateToActivity
import com.admin.ezpark.utils.Utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private val adminViewModel: AdminViewModel by viewModels()
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        splashScreen.setKeepOnScreenCondition { false }
        InsetsUtil.applyInsetsWithInitialPadding(binding.root)

        checkIfAlreadyLogin()
        buttonClicks()
        setObserver()
    }

    private fun checkIfAlreadyLogin() {
        val credentials = sharedPreferencesManager.getBoolean(IS_LOGIN)
        if (credentials)
            callNavigation()
    }

    private fun setObserver() {
        adminViewModel.loginStatus.observe(this) { result ->
            binding.progressBar.visibility = View.GONE
            result.onSuccess {
                showToast(this, "Successfully login!")
                saveLoginInfo()
                callNavigation()
            }
            result.onFailure {
                showToast(this, "Failed to login!")
            }
        }
    }

    private fun saveLoginInfo() {
        sharedPreferencesManager.saveBoolean(IS_LOGIN, true)
    }

    private fun callNavigation() {
        navigateToActivity(this, MainActivity::class.java, finishCurrentActivity = true)
    }

    private fun buttonClicks() {
        binding.buttonLogin.setOnClickListener { loginDetails() }
    }

    private fun loginDetails() {
        val loginID = binding.loginIdText.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        // Validate input
        if (validateInput(loginID, password)) {
            adminViewModel.loginAdminCredentials(loginID, password)
            binding.progressBar.visibility = View.VISIBLE
        }
    }

    private fun validateInput(loginID: String, password: String): Boolean {
        return when {
            loginID.isEmpty() -> {
                showToast(this, "Email cannot be empty")
                false
            }
            password.isEmpty() -> {
                showToast(this, "Password cannot be empty")
                false
            }
            else -> true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}