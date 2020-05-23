package com.d3itb.tournesia.data

import androidx.lifecycle.LiveData
import com.d3itb.tournesia.data.remote.RemoteDataSource
import com.d3itb.tournesia.model.*
import com.d3itb.tournesia.vo.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody

class TournesiaRepository private constructor(private val remoteDataSource: RemoteDataSource) : TournesiaDataSource{
    companion object {
        private var instance: TournesiaRepository? = null

        fun getInstance(remoteDataSource: RemoteDataSource) : TournesiaRepository =
            instance ?: synchronized(this) {
                instance ?: TournesiaRepository(remoteDataSource)
            }
    }

    override fun login(email: String, password: String): LiveData<Resource<Token>> = remoteDataSource.login(email, password)

    override fun register(user: User): LiveData<Resource<Token>> = remoteDataSource.register(user)

    override fun getUser(): LiveData<Resource<User>> = remoteDataSource.getUser()

    override fun getAllPost(): LiveData<Resource<List<Post>>> = remoteDataSource.getAllPost()

    override fun getPostByMe(): LiveData<Resource<List<Post>>> = remoteDataSource.getAllPostByMe()

    override fun getCategory(): LiveData<Resource<List<Category>>> = remoteDataSource.getCategory()

    override fun getProvince(): LiveData<Resource<List<Province>>> = remoteDataSource.getListProvince()

    override fun getCity(id: Int): LiveData<Resource<List<City>>> = remoteDataSource.getListCity(id)

    override fun createPost(
        images: MultipartBody.Part,
        params: HashMap<String, RequestBody>
    ): LiveData<Resource<Post>> {
        return remoteDataSource.createPost(images, params)
    }
}