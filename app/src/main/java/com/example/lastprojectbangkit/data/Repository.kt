package com.example.lastprojectbangkit.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.lastprojectbangkit.data.network.ApiInterceptor
import com.example.lastprojectbangkit.data.network.ApiService
import com.example.lastprojectbangkit.data.network.UserResponse
import com.example.lastprojectbangkit.database.UserScanDatabase
import com.example.lastprojectbangkit.utilities.AppExecutors
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository(
    private val pref: UserPreference,
    private val apiService: ApiService,
    private val userScanDatabase: UserScanDatabase,
    val appExecutors: AppExecutors
) {


    fun getUserToken() : LiveData<String> = pref.getUserToken().asLiveData()
    suspend fun saveUserToken(value: String) = pref.saveUserToken(value)

    fun getUserName() : LiveData<String> = pref.getUserName().asLiveData()
    suspend fun saveUserName(value: String) = pref.saveUserName(value)

    fun getUserEmail() : LiveData<String> = pref.getUserEmail().asLiveData()
    suspend fun saveUserEmail(value: String) = pref.saveUserEmail(value)

    fun getIsFirstTime() : LiveData<Boolean> = pref.isFirstTime().asLiveData()
    suspend fun saveIsFirstTime(value: Boolean) = pref.saveIsFirstTime(value)


    suspend fun clearCache() = pref.clearCache()

    private fun userStories(token: String): ApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(ApiInterceptor(token))
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }


    fun userLogin(email: String, password: String) : Call<UserResponse>  {
        val user: Map<String, String> = mapOf(
            "email" to email,
            "password" to password
        )

        return apiService.userLogin(user)
    }

    fun userRegister(name: String, email: String, password: String) : Call<UserResponse>  {
        val user: Map<String, String> = mapOf(
            "name" to name,
            "email" to email,
            "password" to password
        )

        return apiService.userRegister(user)
    }

    fun uploadStory(
        photo: MultipartBody.Part,
        token: String,
        lat: Float? = null,
        lon: Float? = null): Call<UserResponse> = userStories(token).postUserStory(photo, lat, lon)


   companion object {
        @Volatile
        private var instance: Repository? = null

        @JvmStatic
        fun getInstance(
            pref: UserPreference,
            apiService: ApiService,
            userScanDatabase: UserScanDatabase,
            appExecutors: AppExecutors,
        ) : Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(pref,apiService,userScanDatabase,appExecutors)
            }.also { instance = it }
    }

}