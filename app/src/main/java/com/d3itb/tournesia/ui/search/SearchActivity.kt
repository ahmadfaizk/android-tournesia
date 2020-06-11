package com.d3itb.tournesia.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.d3itb.tournesia.R
import com.d3itb.tournesia.model.Post
import com.d3itb.tournesia.ui.main.home.PostAdapter
import com.d3itb.tournesia.ui.post.PostActivity
import com.d3itb.tournesia.viewmodel.ViewModelFactory
import com.d3itb.tournesia.vo.Status
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    private lateinit var adapter: PostAdapter
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(app_bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[SearchViewModel::class.java]

        adapter = PostAdapter()
        rv_search_result.layoutManager = LinearLayoutManager(this)
        rv_search_result.adapter = adapter

        adapter.setOnClickListener(object : PostAdapter.OnClickListener {
            override fun onCLick(post: Post) {
                val detail = Intent(this@SearchActivity, PostActivity::class.java)
                detail.putExtra(PostActivity.EXTRA_ID, post.id)
                startActivity(detail)
            }
        })

        search_view.isActivated = true
        search_view.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.setName(it) }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.setName(it) }
                return true
            }
        })
        viewModel.posts.observe(this, Observer { response ->
            if (response != null) {
                when(response.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        response.data?.let { adapter.setListPost(it) }
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        showMessage(response.message.toString())
                    }
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progress_bar.visibility = View.VISIBLE
            rv_search_result.visibility = View.GONE
        } else {
            progress_bar.visibility = View.GONE
            rv_search_result.visibility = View.VISIBLE
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}