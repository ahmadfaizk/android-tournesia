package com.d3itb.tournesia.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.d3itb.tournesia.data.TournesiaRepository
import com.d3itb.tournesia.model.Token
import com.d3itb.tournesia.model.User
import com.d3itb.tournesia.vo.Resource

class RegisterViewModel(private val tournesiaRepository: TournesiaRepository): ViewModel() {

    fun register(user: User): LiveData<Resource<Token>> = tournesiaRepository.register(user)
}