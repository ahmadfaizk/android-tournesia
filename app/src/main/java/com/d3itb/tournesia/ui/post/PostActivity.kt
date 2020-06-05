package com.d3itb.tournesia.ui.post

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.d3itb.tournesia.R
import com.d3itb.tournesia.api.ApiClient
import com.d3itb.tournesia.model.Post
import com.d3itb.tournesia.ui.auth.StarterActivity
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
    private var postId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[PostViewModel::class.java]

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
            startActivity(Intent(this, AddCommentActivity::class.java))
        }
    }

    private fun requestData() {
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
                    Status.SUCCESS -> {
                        showMessage("Suskes Menghapus Tempat Ini.")
                        finish()
                    }
                }
            }
        })
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
