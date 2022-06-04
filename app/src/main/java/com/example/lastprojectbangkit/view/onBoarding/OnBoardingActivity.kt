package com.example.lastprojectbangkit.view.onBoarding

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.example.lastprojectbangkit.home.AuthenticationViewModel
import com.example.lastprojectbangkit.view.MainActivity
import com.example.lastprojectbangkit.databinding.ActivityOnBoardingBinding
import com.example.lastprojectbangkit.view.ViewModelFactory

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding
    private lateinit var factory: ViewModelFactory
    private val authenticationViewModel: AuthenticationViewModel by viewModels{factory}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        binding.visitNow.setOnClickListener{
            authenticationViewModel.saveIsFirstTime(false)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        setupView()


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
