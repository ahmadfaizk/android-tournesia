package com.d3itb.tournesia.ui.post

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

        const val REQUEST_UPDATE = 12
        const val RESULT_UPDATE = 22
        const val RESULT_DELETE = 23
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
        btn_comment.setOnClickListener {
            openComment(0f)
        }
        rb_my_votes.setOnRatingBarChangeListener { _, rating, _ ->
            openComment(rating)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.post_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_edit -> {
                val intent = Intent(this, FormActivity::class.java)
                intent.putExtra(FormActivity.EXTRA_ID, postId)
                startActivityForResult(intent, FormActivity.REQUEST_UPDATE)
            }
            R.id.menu_delete -> {
                showDialogDelete()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openComment(votes: Float) {
        val comment = Intent(this, AddCommentActivity::class.java)
        comment.putExtra(AddCommentActivity.EXTRA_ID, postId)
        comment.putExtra(AddCommentActivity.EXTRA_EDIT, isComment)
        comment.putExtra(AddCommentActivity.EXTRA_VOTES, votes)
        if (isComment) {
            startActivityForResult(comment, AddCommentActivity.REQUEST_UPDATE)
        } else {
            startActivityForResult(comment, AddCommentActivity.REQUEST_ADD)
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
        viewModel.comments.observe(this, Observer { response ->
            if (response != null) {
                when(response.status) {
                    Status.SUCCESS -> {
                        val comments = response.data
                        if (comments != null) {
                            commentAdapter.addComments(comments)
                            if (comments.isEmpty()) {
                                tv_ratings_comment_title.text = "Belum Ada Ulasan"
                            }
                        }
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
        val rating = "%.1f".format(post.votes).replace(',', '.')
        tv_votes.text = "$rating - ${post.votesCount} Ulasan"
        rb_rating.rating = post.votes
        tv_description.text = post.description
        tv_author.text = "Diupload Oleh ${post.userName}"
        if (post.isComment != null && post.isComment!!) {
            btn_comment.text = "Edit Ulasan Anda"
            isComment = true
            getMyComment()
        } else {
            btn_comment.text = "Tulis Ulasan"
        }
    }

    private fun getMyComment() {
        viewModel.comment.observe(this, Observer { response ->
            if (response != null) {
                when(response.status) {
                    Status.SUCCESS -> {
                        response.data?.let { populateMyComment(it) }
                    }
                    Status.ERROR -> {
                        showMessage(response.message.toString())
                    }
                }
            }
        })
    }

    private fun populateMyComment(comment: Comment) {
        layout_comment.visibility = View.VISIBLE
        layout_add_comment.visibility = View.GONE
        tv_rating_title.text = "Ulasan Anda"
        tv_name_my_comment.text = comment.userName
        rb_votes_my_comment.rating = comment.votes.toFloat()
        tv_my_comment.text = comment.comment
        if (comment.images.isNotEmpty()) {
            Glide.with(this)
                .load(ApiClient.getImageCommentUrl(comment.images[0].name))
                .into(img_my_comment)
        } else {
            img_my_comment.visibility = View.GONE
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
            showMessage("Berhasil Mengubah Data Tempat")
            setResult(RESULT_UPDATE)
            viewModel.refresh()
        }
        else if (requestCode == AddCommentActivity.REQUEST_ADD && resultCode == AddCommentActivity.RESULT_ADD) {
            showMessage("Berhasil Menambahkan Komentar")
            setResult(RESULT_UPDATE)
            viewModel.refresh()
        }
        else if (requestCode == AddCommentActivity.REQUEST_UPDATE && resultCode == AddCommentActivity.RESULT_UPDATE) {
            showMessage("Berhasil Mengubah Komentar")
            setResult(RESULT_UPDATE)
            viewModel.refresh()
        }
        else if (requestCode == AddCommentActivity.REQUEST_UPDATE && resultCode == AddCommentActivity.RESULT_DELETE) {
            showMessage("Berhasil Menghapus Komentar")
            viewModel.refresh()
        }
    }

    private fun delete() {
        viewModel.deletePost()?.observe(this, Observer { response ->
            if (response != null) {
                when (response.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        setResult(RESULT_DELETE)
                        showMessage("Berhasil Menghapus Tempat")
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
