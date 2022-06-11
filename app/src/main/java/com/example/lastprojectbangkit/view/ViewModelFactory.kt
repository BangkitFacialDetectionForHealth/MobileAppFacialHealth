package com.example.lastprojectbangkit.view

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lastprojectbangkit.data.Repository
import com.example.lastprojectbangkit.data.UserPreference
import com.example.lastprojectbangkit.data.network.ApiConfig
import com.example.lastprojectbangkit.home.AuthenticationViewModel
import com.example.lastprojectbangkit.home.DashboardViewModel
import com.example.lastprojectbangkit.home.HomeViewModel
import com.example.lastprojectbangkit.setting.SettingViewModel

val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(
    name = "settings"
)


class ViewModelFactory private constructor(private val userRepository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> DashboardViewModel() as T
            modelClass.isAssignableFrom(AuthenticationViewModel::class.java) -> AuthenticationViewModel(
                userRepository
            ) as T
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> SettingViewModel(
                userRepository
            ) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(userRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class : ${modelClass.name}")

        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        private var INSTANCE: UserPreference? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    ApiConfig.provideUserRepository(context)
                )
            }.also { instance = it }
    }
}