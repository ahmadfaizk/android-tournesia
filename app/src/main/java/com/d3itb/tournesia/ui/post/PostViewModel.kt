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

    fun setPost(id: Int) {
        postId.value = id
    }

    var post: LiveData<Resource<Post>> = Transformations.switchMap(postId) { id ->
        tournesiaRepository.getPostById(id)
    }

    var comments: LiveData<Resource<List<Comment>>> = Transformations.switchMap(postId) { id ->
        tournesiaRepository.getAllComment(id)
    }

    var comment: LiveData<Resource<Comment>> = Transformations.switchMap(postId) { id ->
        tournesiaRepository.getMyComment(id)
    }

    fun deletePost() : LiveData<Resource<Post>>? = postId.value?.let { tournesiaRepository.deletePost(it) }
}