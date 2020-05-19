package com.d3itb.tournesia.ui.main.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.d3itb.tournesia.data.TournesiaRepository
import com.d3itb.tournesia.model.User
import com.d3itb.tournesia.vo.Resource

class UserViewModel(private val tournesiaRepository: TournesiaRepository): ViewModel() {

    fun getUser(): LiveData<Resource<User>> = tournesiaRepository.getUser()
}