package com.d3itb.tournesia.data.remote

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.d3itb.tournesia.R
import com.d3itb.tournesia.api.ApiClient
import com.d3itb.tournesia.data.remote.response.AuthResponse
import com.d3itb.tournesia.model.Token
import com.d3itb.tournesia.model.User
import com.d3itb.tournesia.utils.TokenPreference
import com.d3itb.tournesia.vo.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource private constructor(private val context: Context){
    private val apiClient = ApiClient.instance
    private val token = TokenPreference.getInstance(context).getToken()

    companion object {
        private var instance: RemoteDataSource? = null

        fun getInstance(context: Context): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(context)
            }
    }

    fun login(email: String, password: String): LiveData<Resource<Token>> {
        val token = MutableLiveData<Resource<Token>>()
        token.value = Resource.loading(null)
        apiClient.login(email, password).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                val error = response.body()?.error
                if (error != null && !error) {
                    token.value = Resource.success(response.body()?.token)
                } else {
                    token.value = Resource.error(response.body()?.message, null)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                token.value = Resource.error(t.message, null)
            }
        })
        return token
    }

    fun register(user: User): LiveData<Resource<Token>> {
        val token = MutableLiveData<Resource<Token>>()
        token.value = Resource.loading(null)
        apiClient.register(user.name, user.email, user.address, user.password!!)
            .enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    val error = response.body()?.error
                    if (error != null && !error) {
                        token.value = Resource.success(response.body()?.token)
                    } else {
                        token.value = Resource.error(response.body()?.message, null)
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    token.value = Resource.error(t.message, null)
                }
            })
        return token
    }

    fun getUser() : LiveData<Resource<User>> {
        val user = MutableLiveData<Resource<User>>()
        apiClient.getUser("Bearer $token").enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                val error = response.body()?.error
                if (error != null && !error) {
                    user.value = Resource.success(response.body()?.data)
                } else {
                    user.value = Resource.error(response.body()?.message, null)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                user.value = Resource.error(t.message, null)
            }
        })
        return user
    }
}