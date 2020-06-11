package com.d3itb.tournesia.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.d3itb.tournesia.data.TournesiaRepository
import com.d3itb.tournesia.model.Post
import com.d3itb.tournesia.vo.Resource

class SearchViewModel(private val tournesiaRepository: TournesiaRepository): ViewModel() {
    val name = MutableLiveData<String>()

    fun setName(name: String) {
        this.name.value = name
    }

    val posts: LiveData<Resource<List<Post>>> = Transformations.switchMap(name) { name ->
        tournesiaRepository.searchPost(name)
    }
}