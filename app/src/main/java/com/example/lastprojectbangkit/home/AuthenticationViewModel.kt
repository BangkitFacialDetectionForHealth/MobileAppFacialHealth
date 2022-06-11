package com.example.lastprojectbangkit.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lastprojectbangkit.data.Repository
import com.example.lastprojectbangkit.data.model.UserModel
import com.example.lastprojectbangkit.data.network.UserResponse
import com.example.lastprojectbangkit.utilities.Event
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthenticationViewModel(private val userRepository: Repository) : ViewModel() {

    private var _user = MutableLiveData<Event<UserModel>>()
    val user: LiveData<Event<UserModel>> = _user

    private var _loading = MutableLiveData<Event<Boolean>>()
    val loading: LiveData<Event<Boolean>> = _loading

    private var _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    private var _error = MutableLiveData<Event<Boolean>>()
    val error: LiveData<Event<Boolean>> = _error

    fun userLogin (
        email:String,
        password:String
    ){
        _loading.value = Event(true)
        val client = userRepository.userLogin(email,password)
        client.enqueue(object : Callback<UserResponse>{
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                Log.e("AuthenticationViewModel", "OnRespone :" + response.body())
                _loading.value = Event(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()?.loginResult
                    _error.value = Event(false)
                    userRepository.appExecutors.networkIO.execute {
                        Log.e("AuthenticationViewModel", "userLogin $responseBody")
                        _user.postValue(Event(responseBody!!))
                    }

                } else {
                    Log.e("AuthenticationViewModel", "OnResponse fail")
                    _message.value = Event(response.message())
                    _error.value = Event(true)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("AuthenticationViewModel", "onFailure: " + t.message)
                _loading.value = Event(false)
                _message.value = Event(t.message.toString())
                _error.value = Event(true)
            }
        })
    }

    fun userRegister(
        name: String,
        email: String,
        password: String
    ) {
        _loading.value = Event(true)
        val client = userRepository.userRegister(name, email, password)
        client.enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.e("AuthenticationViewModel", "onResponse: " + response.body())
                _loading.value = Event(false)
                if (response.isSuccessful) {
                    _error.value = Event(false)
                } else {
                    Log.e("AuthenticationViewModel", "onResponse fail: ")
                    _message.value = Event(response.message())
                    _error.value = Event(true)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("AuthenticationViewModel", "onFailure: " + t.message)
                _loading.value = Event(false)
                _message.value = Event(t.message.toString())
                _error.value = Event(true)
            }
        })
    }

    fun saveUserToken(token: String?) {
        viewModelScope.launch {
            if (token != null) {
                userRepository.saveUserToken(token)
            }
        }
    }

    fun saveUserName(name: String?) {
        viewModelScope.launch {
            if (name != null) {
                userRepository.saveUserName(name)
            }
        }
    }

    fun saveUserEmail(email: String) {
        viewModelScope.launch {
            userRepository.saveUserEmail(email)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.clearCache()
        }
    }

    fun getUserToken() = userRepository.getUserToken()
    fun getIsFirstTime(): LiveData<Boolean> = userRepository.getIsFirstTime()
    fun saveIsFirstTime(value: Boolean) {
        viewModelScope.launch {
            userRepository.saveIsFirstTime(value)
        }
    }
}