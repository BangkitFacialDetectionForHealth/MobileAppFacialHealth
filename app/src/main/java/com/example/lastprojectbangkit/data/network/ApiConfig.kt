package com.example.lastprojectbangkit.data.network

import android.content.Context
import android.util.Log
import com.example.lastprojectbangkit.BuildConfig
import com.example.lastprojectbangkit.data.Repository
import com.example.lastprojectbangkit.data.UserPreference
import com.example.lastprojectbangkit.database.UserStoryDatabase
import com.example.lastprojectbangkit.utilities.AppExecutors
import com.example.lastprojectbangkit.view.dataStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
            fun getApiService(client: OkHttpClient) : ApiService {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
                return retrofit.create(ApiService::class.java)
            }
            fun provideUserRepository(context: Context): Repository {
                val appExecutors = AppExecutors()
                val pref = UserPreference.getInstance(context.dataStore)

                val loggingInterceptor = if(BuildConfig.DEBUG) {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                }
                val client = OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build()

                val userStoryDatabase = UserStoryDatabase.getDatabase(context)

                Log.e("ApiConfig", "Client: $client")
                val apiService = getApiService(client)

                return Repository.getInstance(pref, apiService,userStoryDatabase, appExecutors)
            }
        }
}