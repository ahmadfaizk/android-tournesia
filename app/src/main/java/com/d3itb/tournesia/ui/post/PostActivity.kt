package com.d3itb.tournesia.ui.post

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.d3itb.tournesia.R
import com.d3itb.tournesia.api.ApiClient
import com.d3itb.tournesia.model.Comment
import com.d3itb.tournesia.model.Post
import com.d3itb.tournesia.ui.comment.AddCommentActivity
import com.d3itb.tournesia.ui.main.form.FormActivity
import com.d3itb.tournesia.viewmodel.ViewModelFactory
import com.d3itb.tournesia.vo.Status
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var viewModel: PostViewModel
    private lateinit var commentAdapter: CommentAdapter
    private var postId = 0
    private var isComment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[PostViewModel::class.java]
        commentAdapter = CommentAdapter()
        rv_comment.layoutManager = LinearLayoutManager(this)
        rv_comment.adapter = commentAdapter

        val extras = intent.extras
        if (extras != null) {
            postId = extras.getInt(EXTRA_ID)
            viewModel.setPost(postId)
            requestData()
        }
        btn_edit.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            intent.putExtra(FormActivity.EXTRA_ID, postId)
            startActivityForResult(intent, FormActivity.REQUEST_UPDATE)
        }
        btn_delete.setOnClickListener {
            showDialogDelete()
        }
        btn_comment.setOnClickListener {
            val comment = Intent(this, AddCommentActivity::class.java)
            comment.putExtra(AddCommentActivity.EXTRA_ID, postId)
            comment.putExtra(AddCommentActivity.EXTRA_EDIT, isComment)
            startActivity(comment)
        }
    }

    private fun requestData() {
        viewModel.post.observe(this, Observer { post ->
            if (post != null) {
                when (post.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        post.data?.let { populateData(it) }
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        showMessage(post.message.toString())
                    }
                }
            }
        })
        viewModel.comment.observe(this, Observer { response ->
            if (response != null) {
                when(response.status) {
                    Status.SUCCESS -> {
                        response.data?.let { commentAdapter.addComments(it) }
                    }
                    Status.ERROR -> {
                        showMessage(response.message.toString())
                    }
                }
            }
        })
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
        if (post.isComment != null) {
            btn_comment.text = "Edit Comment"
            isComment = true
        } else {
            btn_comment.text = "Add Comment"
        }
    }

    private fun showDialogDelete() {
        AlertDialog.Builder(this)
            .setMessage("Apakah anda yakin menghapus ini ?")
            .setPositiveButton("Ya"
            ) { _, _ -> delete() }
            .setNegativeButton("Batal"
            ) { dialog, _ -> dialog?.dismiss() }
            .create()
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FormActivity.REQUEST_UPDATE && resultCode == FormActivity.RESULT_UPDATE) {
            showMessage("Berhasil Mengubah Data")
            requestData()
        }
    }

    private fun delete() {
        viewModel.deletePost()?.observe(this, Observer { response ->
            if (response != null) {
                when (response.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        showMessage("Suskes Menghapus Tempat Ini.")
                        finish()
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        showMessage(response.message.toString())
                    }
                }
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progress_bar.visibility = View.VISIBLE
            container.visibility = View.GONE
        } else {
            progress_bar.visibility = View.GONE
            container.visibility = View.VISIBLE
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
