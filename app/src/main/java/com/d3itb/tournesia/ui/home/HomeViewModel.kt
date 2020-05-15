package com.d3itb.tournesia.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.d3itb.tournesia.data.TournesiaRepository
import com.d3itb.tournesia.model.User
import com.d3itb.tournesia.vo.Resource

class HomeViewModel(private val tournesiaRepository: TournesiaRepository): ViewModel() {

    fun getUser(): LiveData<Resource<User>> = tournesiaRepository.getUser()
}