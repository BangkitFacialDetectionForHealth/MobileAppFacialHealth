package com.example.lastprojectbangkit.view
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import java.util.prefs.Preferences

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class ViewModelFactory private constructor(private val userRepository: Repository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(AuthenticationViewModel::class.java) -> AuthenticationViewModel(userRepository) as T
            modelClass.isAssignableFrom(SettingViewModel::class.java)->SettingViewModel(userRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java)->HomeViewModel(userRepository) as T
            modelClass.isAssignableFrom(NewStoryViewModel::class.java)->NewStoryViewModel(userRepository) as T
            modelClass.isAssignableFrom(MapsViewModel::class.java)->MapsViewModel(userRepository) as T
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