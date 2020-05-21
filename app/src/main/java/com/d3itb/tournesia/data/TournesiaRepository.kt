package com.d3itb.tournesia.data

import androidx.lifecycle.LiveData
import com.d3itb.tournesia.data.remote.RemoteDataSource
import com.d3itb.tournesia.model.Post
import com.d3itb.tournesia.model.Token
import com.d3itb.tournesia.model.User
import com.d3itb.tournesia.vo.Resource

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
}