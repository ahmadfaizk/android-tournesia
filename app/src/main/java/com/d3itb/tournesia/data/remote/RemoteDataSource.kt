package com.d3itb.tournesia.data.remote

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.d3itb.tournesia.api.ApiClient
import com.d3itb.tournesia.data.remote.response.*
import com.d3itb.tournesia.model.*
import com.d3itb.tournesia.utils.TokenPreference
import com.d3itb.tournesia.vo.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource private constructor(private val context: Context){
    private val apiClient = ApiClient.instance
    private val token = TokenPreference.getInstance(context).getToken()

    companion object {
        private var instance: RemoteDataSource? = null

        fun getInstance(context: Context): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(context)
            }
    }

    fun login(email: String, password: String): LiveData<Resource<Token>> {
        val token = MutableLiveData<Resource<Token>>()
        token.value = Resource.loading(null)
        apiClient.login(email, password).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                val error = response.body()?.error
                if (error != null && !error) {
                    token.value = Resource.success(response.body()?.data)
                } else {
                    token.value = Resource.error(response.body()?.message, null)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                token.value = Resource.error(t.message, null)
            }
        })
        return token
    }

    fun register(user: User): LiveData<Resource<Token>> {
        val token = MutableLiveData<Resource<Token>>()
        token.value = Resource.loading(null)
        apiClient.register(user.name, user.email, user.password!!, user.address)
            .enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    val error = response.body()?.error
                    if (error != null && !error) {
                        token.value = Resource.success(response.body()?.data)
                    } else {
                        token.value = Resource.error(response.body()?.message, null)
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    token.value = Resource.error(t.message, null)
                }
            })
        return token
    }

    fun getUser() : LiveData<Resource<User>> {
        val user = MutableLiveData<Resource<User>>()
        user.value = Resource.loading(null)
        apiClient.getUser("Bearer $token").enqueue(object : Callback<SingleResponse<User>> {
            override fun onResponse(call: Call<SingleResponse<User>>, response: Response<SingleResponse<User>>) {
                val error = response.body()?.error
                if (error != null && !error) {
                    user.value = Resource.success(response.body()?.data)
                } else {
                    user.value = Resource.error(response.body()?.message, null)
                }
            }

            override fun onFailure(call: Call<SingleResponse<User>>, t: Throwable) {
                user.value = Resource.error(t.message, null)
            }
        })
        return user
    }

    fun getAllPost(): LiveData<Resource<List<Post>>> {
        val listPost = MutableLiveData<Resource<List<Post>>>()
        listPost.value = Resource.loading(null)
        apiClient.getAllPost("Bearer $token").enqueue(object : Callback<MultiResponse<Post>> {
            override fun onResponse(call: Call<MultiResponse<Post>>, response: Response<MultiResponse<Post>>) {
                val error = response.body()?.error
                if (error != null && !error) {
                    listPost.value = Resource.success(response.body()?.data)
                } else {
                    listPost.value = Resource.error(response.body()?.message, null)
                }
            }
            override fun onFailure(call: Call<MultiResponse<Post>>, t: Throwable) {
                listPost.value = Resource.error(t.message, null)
            }
        })
        return listPost
    }

    fun getAllPostByMe(): LiveData<Resource<List<Post>>> {
        val listPost = MutableLiveData<Resource<List<Post>>>()
        listPost.value = Resource.loading(null)
        apiClient.getPostByMe("Bearer $token").enqueue(object : Callback<MultiResponse<Post>> {
            override fun onResponse(call: Call<MultiResponse<Post>>, response: Response<MultiResponse<Post>>) {
                val error = response.body()?.error
                if (error != null && !error) {
                    listPost.value = Resource.success(response.body()?.data)
                } else {
                    listPost.value = Resource.error(response.body()?.message, null)
                }
            }
            override fun onFailure(call: Call<MultiResponse<Post>>, t: Throwable) {
                listPost.value = Resource.error(t.message, null)
            }
        })
        return listPost
    }

    fun getPostById(id: Int): LiveData<Resource<Post>> {
        val post = MutableLiveData<Resource<Post>>()
        post.value = Resource.loading(null)
        apiClient.getPostById("Bearer $token", id).enqueue(object : Callback<SingleResponse<Post>> {
            override fun onResponse(call: Call<SingleResponse<Post>>, response: Response<SingleResponse<Post>>) {
                val error = response.body()?.error
                if (error != null && !error) {
                    post.value = Resource.success(response.body()?.data)
                } else {
                    post.value = Resource.error(response.body()?.message, null)
                }
            }

            override fun onFailure(call: Call<SingleResponse<Post>>, t: Throwable) {
                post.value = Resource.error(t.message, null)
            }
        })
        return post
    }

    fun searchPost(name: String): LiveData<Resource<List<Post>>> {
        val listPost = MutableLiveData<Resource<List<Post>>>()
        listPost.value = Resource.loading(null)
        apiClient.searchPost("Bearer $token", name).enqueue(object : Callback<MultiResponse<Post>> {
            override fun onResponse(call: Call<MultiResponse<Post>>, response: Response<MultiResponse<Post>>) {
                val error = response.body()?.error
                if (error != null && !error) {
                    listPost.value = Resource.success(response.body()?.data)
                } else {
                    listPost.value = Resource.error(response.body()?.message, null)
                }
            }

            override fun onFailure(call: Call<MultiResponse<Post>>, t: Throwable) {
                listPost.value = Resource.error(t.message, null)
            }
        })
        return listPost
    }

    fun getListProvince(): LiveData<Resource<List<Province>>> {
        val listProvince = MutableLiveData<Resource<List<Province>>>()
        listProvince.value = Resource.loading(null)
        apiClient.getProvinces().enqueue(object : Callback<MultiResponse<Province>> {
            override fun onResponse(call: Call<MultiResponse<Province>>, response: Response<MultiResponse<Province>>) {
                val error = response.body()?.error
                if (error != null && !error) {
                    listProvince.value = Resource.success(response.body()?.data)
                } else {
                    listProvince.value = Resource.error(response.body()?.message, null)
                }
            }

            override fun onFailure(call: Call<MultiResponse<Province>>, t: Throwable) {
                listProvince.value = Resource.error(t.message, null)
            }
        })
        return listProvince
    }

    fun getListCity(idProvince: Int): LiveData<Resource<List<Regency>>> {
        val cities = MutableLiveData<Resource<List<Regency>>>()
        cities.value = Resource.loading(null)
        apiClient.getRegencies(idProvince).enqueue(object : Callback<MultiResponse<Regency>> {
            override fun onResponse(call: Call<MultiResponse<Regency>>, response: Response<MultiResponse<Regency>>) {
                val error = response.body()?.error
                if (error != null && !error) {
                    cities.value = Resource.success(response.body()?.data)
                } else {
                    cities.value = Resource.error(response.body()?.message, null)
                }
            }

            override fun onFailure(call: Call<MultiResponse<Regency>>, t: Throwable) {
                cities.value = Resource.error(t.message, null)
            }
        })
        return cities
    }

    fun getCategory(): LiveData<Resource<List<Category>>> {
        val category = MutableLiveData<Resource<List<Category>>>()
        category.value = Resource.loading(null)
        apiClient.getCategory("Bearer $token").enqueue(object : Callback<MultiResponse<Category>> {
            override fun onResponse(call: Call<MultiResponse<Category>>, response: Response<MultiResponse<Category>>) {
                category.value = Resource.success(response.body()?.data)
            }

            override fun onFailure(call: Call<MultiResponse<Category>>, t: Throwable) {
                category.value = Resource.error(t.message, null)
            }
        })
        return category
    }

    fun createPost(images: MultipartBody.Part, params: HashMap<String, RequestBody>) : LiveData<Resource<Post>> {
        val post = MutableLiveData<Resource<Post>>()
        post.value = Resource.loading(null)
        apiClient.createPost("Bearer $token", images, params).enqueue(object : Callback<SingleResponse<Post>> {
            override fun onResponse(call: Call<SingleResponse<Post>>, response: Response<SingleResponse<Post>>) {
                val error = response.body()?.error
                if (error != null && !error) {
                    post.value = Resource.success(response.body()?.data)
                } else {
                    post.value = Resource.error("${response.body()?.message}\n${response.body()?.errors_detail?.joinToString("\n")}", null)
                }
            }

            override fun onFailure(call: Call<SingleResponse<Post>>, t: Throwable) {
                post.value = Resource.error(t.message, null)
            }
        })
        return post
    }

    fun updatePost(idPost: Int, images: MultipartBody.Part?, params: HashMap<String, RequestBody>) : LiveData<Resource<Post>> {
        val post = MutableLiveData<Resource<Post>>()
        post.value = Resource.loading(null)
        apiClient.updatePost("Bearer $token", idPost, images, params).enqueue(object : Callback<SingleResponse<Post>> {
            override fun onResponse(call: Call<SingleResponse<Post>>, response: Response<SingleResponse<Post>>) {
                val error = response.body()?.error
                if (error != null && !error) {
                    post.value = Resource.success(response.body()?.data)
                } else {
                    post.value = Resource.error(response.body()?.message, null)
                }
            }

            override fun onFailure(call: Call<SingleResponse<Post>>, t: Throwable) {
                post.value = Resource.error(t.message, null)
            }
        })
        return post
    }

    fun deletePost(idPost: Int): LiveData<Resource<Post>> {
        val post = MutableLiveData<Resource<Post>>()
        post.value = Resource.loading(null)
        apiClient.deletePost("Bearer $token", idPost).enqueue(object : Callback<SingleResponse<Post>> {
            override fun onResponse(call: Call<SingleResponse<Post>>, response: Response<SingleResponse<Post>>) {
                val error = response.body()?.error
                if (error != null && !error) {
                    post.value = Resource.success(response.body()?.data)
                } else {
                    post.value = Resource.error(response.body()?.message, null)
                }
            }

            override fun onFailure(call: Call<SingleResponse<Post>>, t: Throwable) {
                post.value = Resource.error(t.message, null)
            }
        })
        return post
    }

    fun getAllComment(idPost: Int) : LiveData<Resource<List<Comment>>> {
        val comments = MutableLiveData<Resource<List<Comment>>>()
        comments.value = Resource.loading(null)
        apiClient.getAllCommentByIdPost("Bearer $token", idPost).enqueue(object : Callback<MultiResponse<Comment>> {
            override fun onResponse(call: Call<MultiResponse<Comment>>, response: Response<MultiResponse<Comment>>) {
                val error = response.body()?.error
                if (error != null && !error) {
                    comments.value = Resource.success(response.body()?.data)
                } else {
                    comments.value = Resource.error("${response.body()?.message}\n${response.body()?.errors_detail?.joinToString("\n")}", null)
                }
            }

            override fun onFailure(call: Call<MultiResponse<Comment>>, t: Throwable) {
                comments.value = Resource.error(t.message, null)
            }
        })
        return comments
    }

    fun getMyComment(idPost: Int) : LiveData<Resource<Comment>> {
        val comment = MutableLiveData<Resource<Comment>>()
        comment.value = Resource.loading(null)
        apiClient.getMyCommentByIdPost("Bearer $token", idPost).enqueue(object : Callback<SingleResponse<Comment>> {
            override fun onResponse(call: Call<SingleResponse<Comment>>, response: Response<SingleResponse<Comment>>) {
                val error = response.body()?.error
                if (error != null && !error) {
                    comment.value = Resource.success(response.body()?.data)
                } else {
                    comment.value = Resource.error("${response.body()?.message}\n${response.body()?.errors_detail?.joinToString("\n")}", null)
                }
            }

            override fun onFailure(call: Call<SingleResponse<Comment>>, t: Throwable) {
                comment.value = Resource.error(t.message, null)
            }
        })
        return comment
    }

    fun createComment(idPost: Int, images: MultipartBody.Part?, params: HashMap<String, RequestBody>): LiveData<Resource<Comment>> {
        val comment = MutableLiveData<Resource<Comment>>()
        comment.value = Resource.loading(null)
        apiClient.createComment("Bearer $token", idPost, images, params).enqueue(object : Callback<SingleResponse<Comment>> {
            override fun onResponse(call: Call<SingleResponse<Comment>>, response: Response<SingleResponse<Comment>>) {
                val error = response.body()?.error
                if (error != null && !error) {
                    comment.value = Resource.success(response.body()?.data)
                } else {
                    comment.value = Resource.error("${response.body()?.message}\n${response.body()?.errors_detail?.joinToString("\n")}", null)
                }
            }

            override fun onFailure(call: Call<SingleResponse<Comment>>, t: Throwable) {
                comment.value = Resource.error(t.message, null)
            }
        })
        return comment
    }

    fun updateComment(idPost: Int, images: MultipartBody.Part?, params: HashMap<String, RequestBody>): LiveData<Resource<Comment>> {
        val comment = MutableLiveData<Resource<Comment>>()
        comment.value = Resource.loading(null)
        apiClient.updateComment("Bearer $token", idPost, images, params).enqueue(object : Callback<SingleResponse<Comment>> {
            override fun onResponse(call: Call<SingleResponse<Comment>>, response: Response<SingleResponse<Comment>>) {
                val error = response.body()?.error
                if (error != null && !error) {
                    comment.value = Resource.success(response.body()?.data)
                } else {
                    comment.value = Resource.error(response.body()?.message, null)
                }
            }

            override fun onFailure(call: Call<SingleResponse<Comment>>, t: Throwable) {
                comment.value = Resource.error(t.message, null)
            }
        })
        return comment
    }

    fun deleteComment(idPost: Int) : LiveData<Resource<Comment>> {
        val comment = MutableLiveData<Resource<Comment>>()
        comment.value = Resource.loading(null)
        apiClient.deleteComment("Bearer $token", idPost)
            .enqueue(object : Callback<SingleResponse<Comment>> {
                override fun onResponse(call: Call<SingleResponse<Comment>>, response: Response<SingleResponse<Comment>>) {
                    val error = response.body()?.error
                    if (error != null && !error) {
                        comment.value = Resource.success(response.body()?.data)
                    } else {
                        comment.value = Resource.error(response.body()?.message, null)
                    }
                }

                override fun onFailure(call: Call<SingleResponse<Comment>>, t: Throwable) {
                    comment.value = Resource.error(t.message, null)
                }
            })
        return comment
    }
}