package com.d3itb.tournesia.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.d3itb.tournesia.data.TournesiaRepository
import com.d3itb.tournesia.model.Post
import com.d3itb.tournesia.vo.Resource

class HomeViewModel(private val tournesiaRepository: TournesiaRepository) : ViewModel() {

    private val loadTrigger = MutableLiveData(Unit)

    fun refresh() {
        loadTrigger.value = Unit
    }

    fun getListPost(): LiveData<Resource<List<Post>>> = Transformations.switchMap(loadTrigger) {
        tournesiaRepository.getAllPost()
    }
}