package com.d3itb.tournesia.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.d3itb.tournesia.data.TournesiaRepository
import com.d3itb.tournesia.di.Injection
import com.d3itb.tournesia.ui.auth.login.LoginViewModel
import com.d3itb.tournesia.ui.auth.register.RegisterViewModel
import com.d3itb.tournesia.ui.main.user.UserViewModel

class ViewModelFactory private constructor(private val movieRepository: TournesiaRepository): ViewModelProvider.NewInstanceFactory(){

    companion object {
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(movieRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(movieRepository) as T
            }
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(movieRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class : " + modelClass.name)
        }
    }
}