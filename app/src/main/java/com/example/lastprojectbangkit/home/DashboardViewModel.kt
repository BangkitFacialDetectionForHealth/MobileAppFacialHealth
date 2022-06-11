package com.example.lastprojectbangkit.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {
    val _isNavBarVisible: MutableLiveData<Boolean> = MutableLiveData(true)
    val isNavBarVisible: LiveData<Boolean> = _isNavBarVisible
}