package com.d3itb.tournesia.data

import androidx.lifecycle.LiveData
import com.d3itb.tournesia.model.Post
import com.d3itb.tournesia.model.Token
import com.d3itb.tournesia.model.User
import com.d3itb.tournesia.vo.Resource

interface TournesiaDataSource {

    fun login(email: String, password: String): LiveData<Resource<Token>>

    fun register(user: User): LiveData<Resource<Token>>

    fun getUser(): LiveData<Resource<User>>

    fun getAllPost(): LiveData<Resource<List<Post>>>

    fun getPostByMe(): LiveData<Resource<List<Post>>>
}