package com.d3itb.tournesia.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.d3itb.tournesia.data.TournesiaRepository
import com.d3itb.tournesia.model.Token
import com.d3itb.tournesia.vo.Resource

class LoginViewModel(private val tournesiaRepository: TournesiaRepository): ViewModel() {

    fun login(email: String, password: String): LiveData<Resource<Token>> = tournesiaRepository.login(email, password)

}