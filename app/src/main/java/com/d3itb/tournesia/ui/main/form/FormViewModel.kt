package com.d3itb.tournesia.ui.main.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.d3itb.tournesia.data.TournesiaRepository
import com.d3itb.tournesia.model.Category
import com.d3itb.tournesia.model.Regency
import com.d3itb.tournesia.model.Post
import com.d3itb.tournesia.model.Province
import com.d3itb.tournesia.vo.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FormViewModel(private val tournesiaRepository: TournesiaRepository): ViewModel() {

    private var provinceId = MutableLiveData<Int>()
    private var postId = MutableLiveData<Int>()

    fun setProvinceId(id: Int) {
        provinceId.value = id
    }

    fun setPostId(id: Int) {
        postId.value = id
    }

    var category: LiveData<Resource<List<Category>>> = tournesiaRepository.getCategory()

    var province: LiveData<Resource<List<Province>>> = tournesiaRepository.getProvince()

    var regency: LiveData<Resource<List<Regency>>> = Transformations.switchMap(provinceId) { provinceId ->
        tournesiaRepository.getCity(provinceId)
    }

    var post: LiveData<Resource<Post>> = Transformations.switchMap(postId) { id ->
        tournesiaRepository.getPostById(id)
    }

    fun createPost(images: MultipartBody.Part, params: HashMap<String, RequestBody>): LiveData<Resource<Post>> = tournesiaRepository.createPost(images, params)

    fun updatePost(images: MultipartBody.Part?, params: HashMap<String, RequestBody>): LiveData<Resource<Post>> = tournesiaRepository.updatePost(postId.value!!, images, params)
}