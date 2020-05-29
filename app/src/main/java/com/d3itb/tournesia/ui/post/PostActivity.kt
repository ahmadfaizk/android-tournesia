package com.d3itb.tournesia.ui.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.d3itb.tournesia.R
import com.d3itb.tournesia.api.ApiClient
import com.d3itb.tournesia.model.Post
import com.d3itb.tournesia.viewmodel.ViewModelFactory
import com.d3itb.tournesia.vo.Status
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.activity_post.img_post
import kotlinx.android.synthetic.main.activity_post.tv_address
import kotlinx.android.synthetic.main.activity_post.tv_name

class PostActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var viewModel: PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[PostViewModel::class.java]

        val extras = intent.extras
        if (extras != null) {
            val id = extras.getInt(EXTRA_ID)
            viewModel.setPost(id)
            viewModel.post.observe(this, Observer { post ->
                if (post != null) {
                    when (post.status) {
                        Status.SUCCESS -> {
                            post.data?.let { populateData(it) }
                        }
                    }
                }
            })
        }
    }

    private fun populateData(post: Post) {
        supportActionBar?.title = post.name
        tv_name.text = post.name
        tv_category.text = post.category
        Glide.with(this)
            .load(ApiClient.getImagePostUrl(post.images[0].name))
            .into(img_post)
        tv_address.text = post.address
        tv_region.text = "${post.regency}, ${post.province}"
        tv_votes.text = post.votes.toString()
        tv_description.text = post.description
        tv_author.text = "Diupload Oleh ${post.userName}"
    }
}
