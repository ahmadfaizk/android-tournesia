package com.d3itb.tournesia.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.d3itb.tournesia.data.TournesiaRepository
import com.d3itb.tournesia.model.Post
import com.d3itb.tournesia.vo.Resource

class HomeViewModel(private val tournesiaRepository: TournesiaRepository) : ViewModel() {
    fun getListPost(): LiveData<Resource<List<Post>>> = tournesiaRepository.getAllPost()
}