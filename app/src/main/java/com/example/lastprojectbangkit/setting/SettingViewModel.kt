package com.example.lastprojectbangkit.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.lastprojectbangkit.data.Repository


class SettingViewModel(private val userRepository : Repository) : ViewModel() {
    fun getUserName() : LiveData<String> = userRepository.getUserName()
    fun getUserEmail() : LiveData<String> = userRepository.getUserEmail()


}