package com.example.lastprojectbangkit.view.onBoarding

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.lastprojectbangkit.R
import com.example.lastprojectbangkit.home.AuthenticationViewModel
import com.example.lastprojectbangkit.home.DashboardActivity
import com.example.lastprojectbangkit.home.MainActivity
import com.example.lastprojectbangkit.view.ViewModelFactory

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {

    private lateinit var factory: ViewModelFactory
    private val authenticationViewModel: AuthenticationViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        factory = ViewModelFactory.getInstance(this)
        val delayMillis: Long = 2000
        setupView()

        authenticationViewModel.getIsFirstTime().observe(this) { isFirstTime
            ->
            if (isFirstTime) {
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this@SplashActivity, OnBoardingActivity::class.java)
                    startActivity(intent)
                    finish()
                },delayMillis)
            }else{
                authenticationViewModel.getUserToken().observe(this){ token->
                    if (token.isNullOrEmpty() || token == "not_set_yet"){
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this@SplashActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        },delayMillis)
                    }else{
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this@SplashActivity, DashboardActivity::class.java)
                            startActivity(intent)
                            finish()
                        },delayMillis)
                    }
                }
            }
        }
    }

    private fun setupView() {

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

    }

}


