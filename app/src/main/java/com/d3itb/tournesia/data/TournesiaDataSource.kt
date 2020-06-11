package com.d3itb.tournesia.data

import androidx.lifecycle.LiveData
import com.d3itb.tournesia.model.*
import com.d3itb.tournesia.vo.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface TournesiaDataSource {

    fun login(email: String, password: String): LiveData<Resource<Token>>

    fun register(user: User): LiveData<Resource<Token>>

    fun getUser(): LiveData<Resource<User>>

    fun getAllPost(): LiveData<Resource<List<Post>>>

    fun getPostByMe(): LiveData<Resource<List<Post>>>

    fun getPostById(id: Int): LiveData<Resource<Post>>

    fun searchPost(name: String): LiveData<Resource<List<Post>>>

    fun getCategory(): LiveData<Resource<List<Category>>>

    fun getProvince(): LiveData<Resource<List<Province>>>

    fun getCity(id: Int): LiveData<Resource<List<Regency>>>

    fun createPost(images: MultipartBody.Part, params: HashMap<String, RequestBody>): LiveData<Resource<Post>>

    fun updatePost(idPost: Int, images: MultipartBody.Part?, params: HashMap<String, RequestBody>): LiveData<Resource<Post>>

    fun deletePost(idPost: Int): LiveData<Resource<Post>>

    fun getAllComment(idPost: Int): LiveData<Resource<List<Comment>>>

    fun getMyComment(idPost: Int): LiveData<Resource<Comment>>

    fun createComment(idPost: Int, images: MultipartBody.Part?, params: HashMap<String, RequestBody>): LiveData<Resource<Comment>>

    fun updateComment(idPost: Int, images: MultipartBody.Part?, params: HashMap<String, RequestBody>): LiveData<Resource<Comment>>

    fun deleteComment(idPost: Int): LiveData<Resource<Comment>>
}