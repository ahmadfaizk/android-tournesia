package com.d3itb.tournesia.ui.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.d3itb.tournesia.data.TournesiaRepository
import com.d3itb.tournesia.model.Comment
import com.d3itb.tournesia.model.Post
import com.d3itb.tournesia.vo.Resource

class PostViewModel(private val tournesiaRepository: TournesiaRepository): ViewModel() {
    private var postId = MutableLiveData<Int>()
    private val loadTrigger = MutableLiveData(Unit)

    fun refresh() {
        loadTrigger.value = Unit
    }

    fun setPost(id: Int) {
        postId.value = id
    }

    var post: LiveData<Resource<Post>> = Transformations.switchMap(loadTrigger) {
        postId.value?.let { tournesiaRepository.getPostById(it) }
    }

    var comments: LiveData<Resource<List<Comment>>> = Transformations.switchMap(loadTrigger) {
        postId.value?.let { tournesiaRepository.getAllComment(it) }
    }

    var comment: LiveData<Resource<Comment>> = Transformations.switchMap(loadTrigger) {
        postId.value?.let { tournesiaRepository.getMyComment(it) }
    }

    fun deletePost() : LiveData<Resource<Post>>? = postId.value?.let { tournesiaRepository.deletePost(it) }
}