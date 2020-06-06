package com.d3itb.tournesia.ui.main.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.d3itb.tournesia.R
import com.d3itb.tournesia.model.Post
import com.d3itb.tournesia.ui.post.PostActivity
import com.d3itb.tournesia.viewmodel.ViewModelFactory
import com.d3itb.tournesia.vo.Status
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postAdapter = PostAdapter()
        rv_posts.layoutManager = LinearLayoutManager(context)
        rv_posts.adapter = postAdapter
        rv_posts.setHasFixedSize(true)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[HomeViewModel::class.java]
        viewModel.getListPost().observe(this.viewLifecycleOwner, Observer { data ->
            if (data != null) {
                when(data.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        data.data?.let { postAdapter.setListPost(it) }
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        showMessage(data.message.toString())
                    }
                }
            }
        })
        postAdapter.setOnClickListener(object : PostAdapter.OnClickListener {
            override fun onCLick(post: Post) {
                val intent = Intent(context, PostActivity::class.java)
                intent.putExtra(PostActivity.EXTRA_ID, post.id)
                startActivity(intent)
            }
        })
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progress_bar.visibility = View.VISIBLE
            rv_posts.visibility = View.GONE
        } else {
            progress_bar.visibility = View.GONE
            rv_posts.visibility = View.VISIBLE
        }
    }
}
