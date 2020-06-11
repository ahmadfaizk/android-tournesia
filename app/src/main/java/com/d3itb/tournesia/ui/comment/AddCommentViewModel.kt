package com.d3itb.tournesia.ui.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.d3itb.tournesia.data.TournesiaRepository
import com.d3itb.tournesia.model.Comment
import com.d3itb.tournesia.vo.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddCommentViewModel (private val tournesiaRepository: TournesiaRepository): ViewModel() {
    private var postId = MutableLiveData<Int>()

    fun setPostId(id: Int) {
        postId.value = id
    }

    var comment: LiveData<Resource<Comment>> = Transformations.switchMap(postId) { id ->
        tournesiaRepository.getMyComment(id)
    }

    fun createComment(images: MultipartBody.Part?, params: HashMap<String, RequestBody>): LiveData<Resource<Comment>>? = postId.value?.let { tournesiaRepository.createComment(it, images, params) }

    fun updateComment(images: MultipartBody.Part?, params: HashMap<String, RequestBody>): LiveData<Resource<Comment>>? = postId.value?.let { tournesiaRepository.updateComment(it, images, params) }

    fun deleteComment(): LiveData<Resource<Comment>>? = postId.value?.let { tournesiaRepository.deleteComment(it) }
}